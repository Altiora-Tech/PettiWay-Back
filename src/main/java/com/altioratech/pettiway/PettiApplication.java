package com.altioratech.pettiway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.pettiway.user.infrastructure.security.config")
public class PettiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PettiApplication.class, args);
	}

}
