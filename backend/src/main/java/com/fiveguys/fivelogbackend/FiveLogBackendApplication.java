package com.fiveguys.fivelogbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FiveLogBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiveLogBackendApplication.class, args);
	}

}
