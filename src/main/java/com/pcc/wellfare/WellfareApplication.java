package com.pcc.wellfare;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.pcc.wellfare.model.enums.Role;
import com.pcc.wellfare.repository.UserRepository;
import com.pcc.wellfare.requests.RegisterRequest;
import com.pcc.wellfare.service.AuthenticationService;

@SpringBootApplication
public class WellfareApplication {

	public static void main(String[] args) {
		SpringApplication.run(WellfareApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(
	        AuthenticationService service,
	        UserRepository repository
	) {
	    return args -> {
	        String email = "admin@mail.com";
	        if (repository.existsByEmail(email)) {
	            System.out.println("User with email " + email + " already exists. Skipping registration.");
	            return; // ข้ามการลงทะเบียนหากผู้ใช้มีอยู่แล้ว
	        }

	        var admin = RegisterRequest.builder()
	        		.firstname("Admin")
	        		.lastname("Admin")
	                .email(email)
	                .password("password")
	                .role(Role.ADMIN)
	                .build();
	        System.out.println("Admin token: " + service.register(admin).getAccessToken());
	    };
	}


}
