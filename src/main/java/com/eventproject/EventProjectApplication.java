package com.eventproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventProjectApplication.class, args);
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
//		};
//	}

}
