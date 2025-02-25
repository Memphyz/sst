package com.healthrib.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("imup")
public class ImupController {
	
	@GetMapping
	public String imup() {
		return "I'm up";
	}
	

}
