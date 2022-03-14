package com.eventproject.repository.chatRepo;

import com.eventproject.model.chatModel.ChatNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatNotifRepo extends JpaRepository<ChatNotification, Long> {

}
