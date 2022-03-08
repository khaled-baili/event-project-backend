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
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
//	@Bean
//	CommandLineRunner run(UserService userService) {
//		return args-> {
//			userService.saveRole(new Role(null, "ROLE_ADMIN"));
//			userService.saveRole(new Role(null, "ROLE_VISITOR"));
//			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));
//			userService.saveRole(new Role(null, "ROLE_PRODUCT_OWNER"));
//			userService.saveRole(new Role(null, "ROLE_SPONSOR"));
//
//			userService.saveUser(
//					new User(null,
//							"bailikhaled@gmail.com",
//							"kh446062",
//							"khaled",
//							"baili",
//							new Date(1/6/1998),
//							"50235418",new Role(1L,
//							"ROLE_ADMIN")));
//			userService.saveUser(
//					new User(null,
//							"bailikhaledd@gmail.com",
//							"kh446062",
//							"khaled",
//							"baili",
//							new Date(1/6/1998),
//							"50235418",new Role(1L,
//							"ROLE_VISITOR")));
//			userService.saveUser(
//					new User(null,
//							"bailikhaledDDD@gmail.com",
//							"kh446062",
//							"khaled",
//							"baili",
//							new Date(1/6/1998),
//							"50235418",new Role(1L,
//							"ROLE_PRODUCT_OWNER")));
//		};
//	}

}
