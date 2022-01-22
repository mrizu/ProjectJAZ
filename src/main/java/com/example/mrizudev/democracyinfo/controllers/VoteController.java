package com.example.mrizudev.democracyinfo.controllers;

import com.example.mrizudev.democracyinfo.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class VoteController {
    @Autowired
    VoteService voteService;

}
