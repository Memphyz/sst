package com.healthrib.initializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.healthrib")
@EnableMongoRepositories(basePackages = "com.healthrib.repository")
public class HealthRibInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(HealthRibInitializer.class, args);
	}
}
