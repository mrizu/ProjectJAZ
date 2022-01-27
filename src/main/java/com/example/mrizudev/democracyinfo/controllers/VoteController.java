package com.example.mrizudev.democracyinfo.controllers;

import com.example.mrizudev.democracyinfo.handlers.JwtHandler;
import com.example.mrizudev.democracyinfo.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class VoteController {
    @Autowired
    VoteService voteService;

    @PostMapping("/vote")
    public String submitVote(
            HttpServletResponse response,
            HttpServletRequest request,
            @RequestParam(value = "choice", defaultValue = "") int option_id
    ) {
        try {
            System.out.println("Pierwsze vote");
            String name = JwtHandler.getUsername(JwtHandler.getJwtToken(request));
            System.out.println("drugie vote");
            voteService.addVote(name, option_id);
            System.out.println("trzecie vote");
            int poll_id = voteService.getPollIdByOptionId(option_id);
            response.addCookie(new Cookie("voted_on_"+poll_id, "true"));
            System.out.println("czwarte vote");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/index";
    }



}
