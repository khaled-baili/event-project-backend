package com.eventproject.controller;

import com.eventproject.model.chatModel.ChatMessage;
import com.eventproject.model.chatModel.ChatNotification;
import com.eventproject.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private ChatService chatService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {

        var chatId = chatService.getChatId(chatMessage);
        chatMessage.setChatId(chatId);
        chatService.saveMessage(chatMessage);
        messagingTemplate.convertAndSendToUser(String.valueOf(chatMessage.getRecipient().getUserId()),
                "/queue/messages"
        , new ChatNotification(chatMessage.getIdMessage(), chatMessage.getSender(),
                        chatMessage.getSender().getFirstname()));

    }
}
