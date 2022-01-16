//package com.example.mrizudev.democracyinfo.controllers;
//
//
//import com.example.mrizudev.democracyinfo.model.User;
//import com.example.mrizudev.democracyinfo.services.Authenticator;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class AuthenticatorController {
//    @Autowired
//    Authenticator authenticator;
//
//
//    @GetMapping("/hello")
//    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
//            return String.format("Hello %s!", name);
//    }
//
//    @GetMapping("/user")
//        public User getUser(@RequestParam(value = "id") int id) {
//            User user = authenticator.getUser(id);
//            return user;
//    }
//
//    @GetMapping("/login")
//    public String login(
//            @RequestParam(value = "username", defaultValue = "") String username,
//            @RequestParam(value = "password", defaultValue = "") String password
//    ) {
//        if (username.equals("") || password.equals("")) return "No username or password provided";
//        String message = authenticator.authenticate(username, password);
//        return message;
//    }
//
//
//    @GetMapping("/error")
//    public String error() {
//        return "Error occured!";
//    }
//
//
//}
