package com.eazybytes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableMethodSecurity(jsr250Enabled = true,prePostEnabled = true,securedEnabled = true)
public class EasyBankBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(EasyBankBackendApplication.class, args);
	}
}
