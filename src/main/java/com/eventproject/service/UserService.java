package com.eventproject.service;

import com.eventproject.model.Role;
import com.eventproject.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    User getUser(String email);
    Boolean checkUserExist(String email);
    List<User>getUsers();
    Boolean verifyCode(String verficationCode);
    User findUserByResetToken(String resetToken);
    Boolean updateResetToken(String email, String passwordResetLink);


}
