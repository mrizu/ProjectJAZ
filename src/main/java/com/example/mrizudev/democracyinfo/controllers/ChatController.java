package com.example.mrizudev.democracyinfo.controllers;

import com.example.mrizudev.democracyinfo.handlers.JwtHandler;
import com.example.mrizudev.democracyinfo.model.ChatMessage;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("sender", chatMessage.getSender());
        //headerAccessor.getSessionAttributes().put("color", new JwtHandler().getPartyColorByUsername(chatMessage.getSender()));
        return chatMessage;
    }
}
