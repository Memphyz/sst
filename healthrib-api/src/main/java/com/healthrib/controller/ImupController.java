package com.healthrib.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("imup")
public class ImupController {
	
	@GetMapping
	public String imup() throws MessagingException, IOException {
		log.info("GET | imup | Checking if i'm up");
		return "I'm up";
	}
	

}
