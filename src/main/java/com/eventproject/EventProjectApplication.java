package com.eventproject;

import com.eventproject.model.Role;
import com.eventproject.model.User;
import com.eventproject.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class EventProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventProjectApplication.class, args);
	}


}
