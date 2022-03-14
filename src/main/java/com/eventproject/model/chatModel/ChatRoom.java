package com.eventproject.model.chatModel;

import com.eventproject.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idRoom;
    private String chatId;
    @OneToOne(fetch = FetchType.EAGER)
    private User sender;
    @OneToOne(fetch = FetchType.EAGER)
    private User recipient;
}
