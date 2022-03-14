package com.eventproject.service;

import com.eventproject.model.chatModel.ChatMessage;
import com.eventproject.model.chatModel.ChatNotification;
import com.eventproject.model.chatModel.ChatRoom;
import com.eventproject.repository.chatRepo.ChatMessageRepo;
import com.eventproject.repository.chatRepo.ChatNotifRepo;
import com.eventproject.repository.chatRepo.ChatRoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    @Autowired
    private ChatNotifRepo chatNotifRepo;

    @Autowired
    private ChatRoomRepo chatRoomRepo;

    @Autowired
    private ChatMessageRepo chatMessageRepo;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        return chatMessageRepo.save(chatMessage);
    }
    public void deleteMessage(ChatMessage chatMessage) {
        chatMessageRepo.delete(chatMessage);
    }

    public ChatRoom chatRoom(ChatRoom chatRoom) {
        return chatRoomRepo.save(chatRoom);
    }

    public void deleteChatRoom(ChatRoom chatRoom) {
        chatRoomRepo.delete(chatRoom);
    }

    public ChatNotification saveNotif(ChatNotification chatNotification) {
        return chatNotifRepo.save(chatNotification);
    }

    public String getChatId(ChatMessage chatMessage) {
        String recipientId = String.valueOf(chatMessage.getRecipient().getUserId());
        String senderId = String.valueOf(chatMessage.getSender().getUserId());
        return recipientId + senderId;
    }
}
