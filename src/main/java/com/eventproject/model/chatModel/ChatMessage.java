package com.eventproject.model.chatModel;

import com.eventproject.enumType.MessageStatus;
import com.eventproject.model.actorModel.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idMessage;
    private String chatId;
    @OneToOne(fetch = FetchType.EAGER)
    private ChatRoom chatRoom;
    @OneToOne(fetch = FetchType.EAGER)
    private User sender;
    @OneToOne(fetch = FetchType.EAGER)
    private User recipient;
    private String content;
    private Date timesstamp;
    private MessageStatus status;


}
