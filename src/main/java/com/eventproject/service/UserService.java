package com.eventproject.service;

import com.eventproject.model.Role;
import com.eventproject.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    User getUser(String email);
    Boolean checkUserExist(String email);
    List<User>getUsers();
    void sendVerficationEmail(User user, String siteURL);
    Boolean verifyCode(String verficationCode);

}
