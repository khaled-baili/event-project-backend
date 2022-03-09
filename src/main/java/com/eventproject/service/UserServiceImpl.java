package com.eventproject.service;

import com.eventproject.model.Role;
import com.eventproject.model.User;
import com.eventproject.model.Visitor;
import com.eventproject.repository.RoleRepo;
import com.eventproject.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.sun.activation.registries.LogSupport.log;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

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
    public void sendVerficationEmail(User user, String siteURL) {
        try {
            String toAddress = user.getEmail();
            String fromAddress = "eventPmanager@gmail.com";
            String senderName = "CamelSoft";
            String subject = "Please verify your registration";
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Thank you,<br>"
                    + "Your company name.";

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getFirstname());
            String verifyURL = siteURL + "/verify/" + user.getVerificationCode();
            content = content.replace("[[URL]]", verifyURL);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception exception) {
            log(exception.getMessage());
        }
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

}
