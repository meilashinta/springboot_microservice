package com.meila.peminjaman_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// Beri tahu Spring untuk HANYA mencari JPA Repository di package ini
@EnableJpaRepositories(basePackages = "com.meila.peminjaman_service.repository.jpa")
// Beri tahu Spring untuk HANYA mencari Mongo Repository di package ini
@EnableMongoRepositories(basePackages = "com.meila.peminjaman_service.repository.mongo")
public class PeminjamanServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeminjamanServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
