package com.healthrib.authorization;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.healthrib.model.user.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(PER_CLASS)
public class AuthorizationColtrollerTest {
	
	private static final User user = new User();
	
	@BeforeAll
	private void create_user_and_sign_up() {
		user.setName("JUnit");
		user.setPassword("junit@2025");
		user.setEmail("junit@healthribbeforeallsignup.com");
		
	}

}
