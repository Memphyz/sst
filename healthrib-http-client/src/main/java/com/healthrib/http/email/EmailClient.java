package com.healthrib.http.email;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "healthrib", url = "http://localhost:8080")
public interface EmailClient {
	
	@PostMapping("/api/v1/email/send/{name}/{email}")
	public ResponseEntity<Void> send(@PathVariable String name, @PathVariable String email) throws Exception;
}
