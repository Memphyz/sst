package com.healthrib.authorization;

import static com.healthrib.enums.user.permission.UserPermissionType.TEST;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.healthrib.TestConfig;
import com.healthrib.resources.Credentials;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestConfig.class)
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(PER_CLASS)
public class AuthorizationColtrollerTest {
	
	private static final Credentials credentials = new Credentials();
	
	@BeforeAll
	public void create_credentials() {
		log.info("AuthorizationColtrollerTest | create_credentials | Criando usuário com dados mockados para testes");
		credentials.setName("JUnit");
		credentials.setPassword("junit@2025");
		credentials.setEmail("junit@healthribbeforeallsignup.com");
		credentials.setRoles(asList(TEST));
	}
	
	@Test
	@Order(0)
	public void should_verify_credentials_data_not_null() {
		assertNotNull(credentials.getName());
		assertNotNull(credentials.getPassword());
		assertNotNull(credentials.getEmail());
		assertNotNull(credentials.getRoles());
	}
	
	@Test
	@Order(1)
	public void should_verify_credentials_data_mock() {
		assertEquals(credentials.getName(), "JUnit");
		assertEquals(credentials.getPassword(), "junit@2025");
		assertEquals(credentials.getEmail(), "junit@healthribbeforeallsignup.com");
		assertEquals(credentials.getRoles(), asList(TEST));
	}

}
