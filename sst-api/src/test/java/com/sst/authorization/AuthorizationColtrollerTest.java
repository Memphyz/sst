package com.sst.authorization;

import static com.sst.enums.user.permission.UserPermissionType.TEST;
import static com.sst.utils.RestAssuredUtil.delete;
import static com.sst.utils.RestAssuredUtil.get;
import static com.sst.utils.RestAssuredUtil.post;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import com.sst.TestConfig;
import com.sst.resources.Credentials;
import com.sst.resources.Login;
import com.sst.resources.Token;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestInstance(PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(classes = TestConfig.class)
public class AuthorizationColtrollerTest {
	
	private static final Credentials credentials = new Credentials();
	private static final Map<String, String> authHeader = new HashMap<>();
	
	@Autowired
	Environment environment;
	
	@Value("${account-test.admin.email}")
	private String adminEmail;
	
	@Value("${account-test.admin.password}")
	private String adminPassword;
	
	@BeforeAll
	public void prepare_tests() {
		log.info("AuthorizationColtrollerTest | prepare_tests | Criando usuário com dados mockados para testes");
		credentials.setName("JUnit");
		credentials.setPassword("junit@2025");
		credentials.setEmail("junit@sstbeforeallsignup.com");
		credentials.setRoles(asList(TEST));
		log.info("AuthorizationColtrollerTest | prepare_tests | Obtendo token de ADMINISTRADOR para realizar alterações no banco");
		Token token = post("/authorization/signin", new Login(adminEmail, adminPassword) ,Token.class);
		authHeader.put("Authorization", "Bearer " + token.getAccessToken());
		assertNotNull(token);
		assertNotNull(token.getAccessToken());
		log.info("AuthorizationColtrollerTest | prepare_tests | Token obtido com sucesso!");
		log.info("AuthorizationColtrollerTest | prepare_tests | Verificando se o usuário de testes existe");
		Response response = get("/api/v1/user/email/" + credentials.getEmail(), authHeader);
		if(response.getStatusCode() == FORBIDDEN.value()) {
			log.info("AuthorizationColtrollerTest | prepare_tests | Usuário {} não existe na base de dados, tudo certo.", credentials.getEmail());
			return;
		}
		log.info("AuthorizationColtrollerTest | prepare_tests | Usuário {} existe na base de dados, realizando limpeza.", credentials.getEmail());
		delete("/api/v1/user/email/" + credentials.getEmail(), authHeader);
		log.info("AuthorizationColtrollerTest | prepare_tests | Limpeza concluída");
		
	}
	
	@Test
	@Order(0)
	public void should_verify_credentials_data_not_null() {
		log.info("AuthorizationColtrollerTest | should_verify_credentials_data_not_null | Verificando se as variáveis cruciais para os testes não estão nulas");
		assertNotNull(credentials.getName());
		assertNotNull(credentials.getPassword());
		assertNotNull(credentials.getEmail());
		assertNotNull(credentials.getRoles());
	}
	
	@Test
	@Order(1)
	public void should_verify_credentials_data_mock() {
		log.info("AuthorizationColtrollerTest | should_verify_credentials_data_mock | Verifica se os dados do mock estão corretos");
		assertEquals(credentials.getName(), "JUnit");
		assertEquals(credentials.getPassword(), "junit@2025");
		assertEquals(credentials.getEmail(), "junit@sstbeforeallsignup.com");
		assertEquals(credentials.getRoles(), asList(TEST));
	}
	
	@Test
	@Order(2)
	public void should_delete_test_user_if_he_exists() {
		
	}
	

}
