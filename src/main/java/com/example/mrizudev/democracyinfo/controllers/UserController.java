package com.example.mrizudev.democracyinfo.controllers;

import com.example.mrizudev.democracyinfo.model.User;
import com.example.mrizudev.democracyinfo.services.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    Authenticator authenticator;

    @GetMapping("/login")
    public String displayLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String submitLogin(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password)
 {
        if (username.equals("") || password.equals("")) return "No username or password provided";
        String message = authenticator.authenticate(username, password);
        return message;
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    // Login form with error
    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

}
