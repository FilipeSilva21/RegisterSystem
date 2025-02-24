package com.registerSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class RegisterSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterSystemApplication.class, args);
	}

}
