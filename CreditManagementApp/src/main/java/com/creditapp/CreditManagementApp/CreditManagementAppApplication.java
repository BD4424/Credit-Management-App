package com.creditapp.CreditManagementApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CreditManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditManagementAppApplication.class, args);
	}

}
