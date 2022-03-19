package com.eventproject.service;

import com.eventproject.model.actorModel.Role;
import com.eventproject.model.actorModel.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    User getUser(String email);
    Boolean checkUserExist(String email);
    List<User>getUsers();
    Boolean verifyCode(String verficationCode);
    User findUserByResetToken(String resetToken);
    Boolean updateResetToken(String email, String passwordResetLink);
    User findUserById(long userId);


}
