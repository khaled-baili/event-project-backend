package com.eventproject.model;

import com.eventproject.model.actorModel.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private User user;
    private String from;
    private String subject;
    private String siteUrl;

}
