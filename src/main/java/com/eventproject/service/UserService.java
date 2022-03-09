package com.eventproject.service;

import com.eventproject.model.Role;
import com.eventproject.model.User;
import com.eventproject.model.Visitor;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Visitor saveVisitor(Visitor visitor);
    Role saveRole(Role role);
    User getUser(String email);
    Boolean checkUserExist(String email);
    List<User>getUsers();

}
