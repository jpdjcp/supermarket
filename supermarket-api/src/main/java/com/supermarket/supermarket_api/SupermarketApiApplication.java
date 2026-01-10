package com.supermarket.supermarket_api;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SupermarketApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupermarketApiApplication.class, args);
	}

	@PostConstruct
	public void started() {
		System.out.println("🚀 Supermarket API STARTED");
	}
}
