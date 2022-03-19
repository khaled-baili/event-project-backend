package com.eventproject.repository;

import com.eventproject.model.actorModel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);
    User findByVerificationCode(String verificationCode);
    User findByResetToken(String restToken);
    User findUserByUserId(long userId);
}
