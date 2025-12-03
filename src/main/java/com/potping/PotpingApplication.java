package com.potping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PotpingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PotpingApplication.class, args);
	}

}
