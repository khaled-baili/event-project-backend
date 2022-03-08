package com.eventproject.service;

import com.eventproject.model.Role;
import com.eventproject.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    User getUser(String email);
    List<User>getUsers();

}
