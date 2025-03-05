package com.healthrib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication(scanBasePackages = "com.healthrib")
@EnableMongoRepositories(basePackages = "com.healthrib.repository")
@OpenAPIDefinition(info = @Info(title = "HealthRib API", version = "1.0", description = "Documentation of HealthRib API", license = @License(name = "Apache 2.0", url = "http://springdoc.org")))
public class HealthRibInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HealthRibInitializer.class, args);
	}
}
