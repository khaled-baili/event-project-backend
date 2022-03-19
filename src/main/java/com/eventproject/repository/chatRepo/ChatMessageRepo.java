package com.eventproject.repository.chatRepo;

import com.eventproject.model.actorModel.User;
import com.eventproject.model.chatModel.ChatMessage;
import com.eventproject.model.chatModel.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepo extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> getChatMessagesByChatRoom(ChatRoom chatRoom);
    List<ChatMessage> getChatMessagesBySender(User user);
    List<ChatMessage> getChatMessagesByRecipient(User user);
    ChatMessage deleteChatMessageBySender(User user);
    ChatMessage deleteChatMessageByRecipient(User user);
    ChatMessage deleteChatMessageByIdMessage(Long idMessage);


}
