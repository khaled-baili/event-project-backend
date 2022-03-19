package com.eventproject.model.chatModel;

import com.eventproject.model.actorModel.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idNotif;
    @OneToOne(fetch = FetchType.EAGER)
    private User sender;
    private String senderName;
}
