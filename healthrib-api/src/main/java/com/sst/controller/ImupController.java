package com.sst.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("imup")
@Tag(name = "Imup", description = "Used to check if API is healthly and running")
public class ImupController {
	
	@GetMapping
	@Operation(description = "Returns a string with status os API")
	public String imup() throws MessagingException, IOException {
		log.info("GET | imup | Checking if i'm up");
		return "I'm up";
	}
	

}
