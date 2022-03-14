package com.eventproject.service;

import com.eventproject.model.Mail;
import com.eventproject.model.Role;
import com.eventproject.model.User;
import com.eventproject.repository.RoleRepo;
import com.eventproject.repository.UserRepo;
import com.sun.mail.imap.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



import javax.transaction.Transactional;
import java.util.*;


@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            UserServiceImpl.log.error("user not found in database");
            throw new UsernameNotFoundException("User not found in databse");
        } else if (!user.isEnabled()){
            UserServiceImpl.log.error("account not yet activated");
            throw new UsernameNotFoundException("account not yet activated");
        } else if (user.getAccountStatus() == 0) {
            UserServiceImpl.log.error("account is banned contact website admin");
            throw new UsernameNotFoundException("account is banned contact website admin");
        }
        else {
            UserServiceImpl.log.info("User found in databse : {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority((user.getRole().getName())));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
    @Override
    public Boolean checkUserExist(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        UserServiceImpl.log.info("saving new user : {}", user.getEmail());
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }


    @Override
    public User getUser(String email) {
        UserServiceImpl.log.info("getting user");
        return userRepo.findByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }


    @Override
    public Boolean verifyCode(String verficationCode) {
        User user = userRepo.findByVerificationCode(verficationCode);
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepo.save(user);
            return true;
        }
    }

    @Override
    public User findUserByResetToken(String resetToken) {
        return userRepo.findByResetToken(resetToken);
    }

    public Boolean updateResetToken(String email,String urlResetPasswordToken) {
        User user = userRepo.findByEmail(email);
        String token = RandomString.make(36);
        if (user != null) {
            user.setResetToken(token);
            userRepo.save(user);
            Mail mail = new Mail(
                    user,"CamelSoft","Reset Password",
                    urlResetPasswordToken+user.getResetToken());
            emailService.sendEmail(mail);
            return true;
        } else return false;
    }

    public User getByResetToken(String token) {
        return userRepo.findByResetToken(token);
    }


}
