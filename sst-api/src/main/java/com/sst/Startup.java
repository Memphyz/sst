package com.sst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@EnableScheduling
@EnableFeignClients(basePackages = "com.sst")
@SpringBootApplication(scanBasePackages = "com.sst")
@EnableMongoRepositories(basePackages = "com.sst.repository")
@OpenAPIDefinition(info = @Info(title = "SST API", version = "1.0", description = "Documentation of SST API", license = @License(name = "Apache 2.0", url = "http://springdoc.org")))
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}
}
