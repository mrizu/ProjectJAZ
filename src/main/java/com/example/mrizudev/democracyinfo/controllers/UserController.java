package com.example.mrizudev.democracyinfo.controllers;


import com.example.mrizudev.democracyinfo.services.UserService;
import com.example.mrizudev.democracyinfo.handlers.JwtHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String displayLogin(
            HttpServletRequest request
    ) {
        if (JwtHandler.verifyToken(request)) return "index";
        return "login";
    }

    @PostMapping("/login")
    public String submitLogin(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            HttpServletResponse response)
    {
        if (username.equals("") || password.equals("")) {
            response.addCookie(new Cookie("error_authenticating", "true"));
            return "login";
        }
        String message = userService.authenticate(username, password);
        if (message.equals("error")) {
            response.addCookie(new Cookie("error_authenticating", "true"));
            return "login";
        } else {
            response.addCookie(new Cookie("Bearer", message));
            return "index";
        }
    }

    @GetMapping("/register")
    public String displayRegister(
            HttpServletRequest request
    ) {
        if (JwtHandler.verifyToken(request)) return "index";
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        response.addCookie(new Cookie("Bearer", ""));
        return "login";
    }

    @PostMapping("/register")
    public String submitRegister(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam(value = "party", defaultValue = "") String party_name,
            HttpServletResponse response
            ) {
                if (username.equals("") || password.equals("") || email.equals("")) {
                    response.addCookie(new Cookie("error_authenticating", "true"));
                    return "register";
                }
                try {
                    userService.register(username, password, email, party_name);
                    String message = userService.authenticate(username, password);
                    response.addCookie(new Cookie("Bearer", message));
                    return "index";
                } catch (Exception e) {
                    response.addCookie(new Cookie("error_authenticating", "true"));
                    return "register";
                }
            }

    @RequestMapping("/index")
    public String index(
            HttpServletRequest request,
            Model model
    ) {
        if (!JwtHandler.verifyToken(request)) return "login";
        String name = JwtHandler.getUsername(JwtHandler.getJwtToken(request));
        if (name == null) return "login";
        model.addAttribute("name", name);
        return "index";
    }

}
