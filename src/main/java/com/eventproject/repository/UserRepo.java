package com.eventproject.repository;

import com.eventproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);
    User findByVerificationCode(String verificationCode);
}
