package com.example.mrizudev.democracyinfo.controllers;

import com.example.mrizudev.democracyinfo.handlers.JwtHandler;
import com.example.mrizudev.democracyinfo.model.ChatMessage;
import com.example.mrizudev.democracyinfo.repositories.UserRepository;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
@Controller
public class ChatController {
    @PersistenceContext
    EntityManager entityManager;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String sender = chatMessage.getSender();
        String senderColor = JwtHandler.getPartyColorByUsername(entityManager, sender);
        chatMessage.setSender("{\"sender\":\"" + sender + "\", \"color\":\""+senderColor+"\"}");
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("sender", chatMessage.getSender());
        String sender = chatMessage.getSender();
        String senderColor = JwtHandler.getPartyColorByUsername(entityManager, sender);
        chatMessage.setSender("{\"sender\":\"" + sender + "\", \"color\":\""+senderColor+"\"}");
        return chatMessage;
    }
}
