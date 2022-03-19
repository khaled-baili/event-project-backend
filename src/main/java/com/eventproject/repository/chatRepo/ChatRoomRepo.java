package com.eventproject.repository.chatRepo;

import com.eventproject.model.actorModel.User;
import com.eventproject.model.chatModel.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepo extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> getChatRoomsBySender(User user);
    List<ChatRoom> getChatRoomsByRecipient(User user);
    ChatRoom deleteChatRoomBySender(User user);
    ChatRoom deleteChatRoomByRecipient(User user);


}
