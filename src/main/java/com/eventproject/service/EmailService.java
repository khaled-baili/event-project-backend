package com.eventproject.service;

import com.eventproject.model.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;


@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendEmail(Mail mail) {
        try {
            String toAddress = mail.getUser().getEmail();
            String fromAddress = mail.getFrom();
            String senderName = mail.getUser().getFirstname();
            String subject = mail.getSubject();
            String content = "Dear [[name]],<br>"
                    + "Please click the link below :<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Thank you,<br>"
                    + "Your company name.";
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[name]]", mail.getUser().getFirstname());
            String verifyURL = mail.getSiteUrl();
            content = content.replace("[[URL]]", verifyURL);
            helper.setText(content, true);
            emailSender.send(message);
        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }
}
