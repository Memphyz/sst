package com.sst.authorization;

import static com.sst.enums.ValidationMessagesType.USER_VERIFIED;
import static com.sst.enums.user.permission.UserPermissionType.ADMINISTRATOR;
import static com.sst.utils.RestAssuredUtil.delete;
import static com.sst.utils.RestAssuredUtil.get;
import static com.sst.utils.RestAssuredUtil.post;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.sst.TestConfig;
import com.sst.enums.ValidationMessagesType;
import com.sst.model.user.ConfirmationToken;
import com.sst.model.user.RecoveryPassword;
import com.sst.model.user.User;
import com.sst.repository.user.ConfirmationTokenRepository;
import com.sst.repository.user.RecoveryPasswordRepository;
import com.sst.resources.Credentials;
import com.sst.resources.Login;
import com.sst.resources.ResetPassword;
import com.sst.resources.Token;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestConfig.class)
@TestInstance(PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class AuthorizationColtrollerTest {

	private static final Credentials credentials = new Credentials();
	private static final Map<String, String> authHeader = new HashMap<>();
	private Token token;
	private User user;

	@Autowired
	private ConfirmationTokenRepository repository;
	
	@Autowired
	private RecoveryPasswordRepository recoveryRepository;

	@Value("${account-test.admin.email}")
	private String adminEmail;

	@Value("${account-test.admin.password}")
	private String adminPassword;
	
	@Value("${account-test.admin.new-password}")
	private String newPassword;

	@BeforeAll
	public void prepare_tests() {
		log.info("AuthorizationColtrollerTest | prepare_tests | Criando usuário com dados mockados para testes");
		credentials.setName("JUnit");
		credentials.setPassword("junit@2025");
		credentials.setEmail("junit@sstbeforeallsignup.com");
		credentials.setRoles(asList(ADMINISTRATOR));
		log.info("AuthorizationColtrollerTest | prepare_tests | Obtendo token de ADMINISTRADOR para realizar alterações no banco");
		Token token = post("/authorization/signin", new Login(adminEmail, adminPassword), Token.class);
		authHeader.put("Authorization", "Bearer " + token.getAccessToken());
		assertNotNull(token);
		assertNotNull(token.getAccessToken());
		log.info("AuthorizationColtrollerTest | prepare_tests | Token obtido com sucesso!");
		log.info("AuthorizationColtrollerTest | prepare_tests | Verificando se o usuário de testes existe");
		Response response = get("/api/v1/user/email/" + credentials.getEmail(), authHeader);
		if (response.getStatusCode() == FORBIDDEN.value()) {
			log.info("AuthorizationColtrollerTest | prepare_tests | Usuário {} não existe na base de dados, tudo certo.",
					credentials.getEmail());
			return;
		}
		log.info("AuthorizationColtrollerTest | prepare_tests | Usuário {} existe na base de dados, realizando limpeza.",
				credentials.getEmail());
		delete("/api/v1/user/email/" + credentials.getEmail(), authHeader);
		log.info("AuthorizationColtrollerTest | prepare_tests | Limpeza concluída");
	}
	
	@AfterAll
	public void finish_tests() {
		log.info("AuthorizationColtrollerTest | finish_tests | Deletando usuario {}", user.getEmail());
		delete("/api/v1/user/email/" + user.getEmail(), authHeader);
		log.info("AuthorizationColtrollerTest | finish_tests | Finalizando testes...");
	}

	@Test
	@Order(1)
	public void should_verify_credentials_data_not_null() {
		log.info("AuthorizationColtrollerTest | should_verify_credentials_data_not_null | Verificando se as variáveis cruciais para os testes não estão nulas");
		assertNotNull(credentials.getName());
		assertNotNull(credentials.getPassword());
		assertNotNull(credentials.getEmail());
		assertNotNull(credentials.getRoles());
	}

	@Test
	@Order(2)
	public void should_verify_credentials_data_mock() {
		log.info("AuthorizationColtrollerTest | should_verify_credentials_data_mock | Verifica se os dados do mock estão corretos");
		assertEquals(credentials.getName(), "JUnit");
		assertEquals(credentials.getPassword(), "junit@2025");
		assertEquals(credentials.getEmail(), "junit@sstbeforeallsignup.com");
		assertEquals(credentials.getRoles(), asList(ADMINISTRATOR));
	}

	@Test
	@Order(3)
	public void create_test_user() {
		log.info("AuthorizationColtrollerTest | create_test_user | Criando usuario para realizar testes");
		Response response = post("/authorization/signup", credentials);
		assertEquals(response.getStatusCode(), CREATED.value());
	}

	@Test
	@Order(4)
	public void sign_in_test_user() {
		log.info("AuthorizationColtrollerTest | sign_in_test_user | Realiza login do usuário recem criado");
		token = post("/authorization/signin", new Login(credentials.getEmail(), credentials.getPassword()),
				Token.class);
		authHeader.clear();
		authHeader.put("Authorization", token.getAccessToken());
		assertInstanceOf(Token.class, token);
	}
	
	@Test
	@Order(5)
	public void should_check_signedin_token() {
		log.info("AuthorizationColtrollerTest | should_check_signedin_token | Verifica token do usuário {}", credentials.getEmail());
		assertNotNull(token.getAccessToken());
		assertNotNull(token.getRefreshToken());
		assertEquals(token.getEmail(), credentials.getEmail());
		assertNotNull(token.getExpiration());
	}

	@Test
	@Order(6)
	public void should_find_test_user() {
		log.info("AuthorizationColtrollerTest | should_find_test_user | Busca o usuário de teste recém criado");
		user = get("/api/v1/user/email/" + credentials.getEmail(), User.class, authHeader);
		assertInstanceOf(User.class, user);
		assertEquals(user.getEmail(), credentials.getEmail());
	}

	@Test
	@Order(7)
	public void assert_test_user_not_verified() {
		log.info("AuthorizationColtrollerTest | assert_test_user_not_verified | Checando se o email do usuário não está verificado");
		assertFalse(user.isVerified());
	}

	@Test
	@Order(8)
	public void verify_email_confirmation_test_user() {
		log.info("AuthorizationColtrollerTest | verify_test_user | Realiza a verificação de email do usuário recém criado");
		ConfirmationToken token = repository.findByEmail(user.getEmail());
		ValidationMessagesType response = post("/authorization/verify/" + user.getEmail() + "/" + token.getConfirmationToken(),
				ValidationMessagesType.class);
		user = get("/api/v1/user/email/" + credentials.getEmail(), User.class, authHeader);
		assertEquals(response, USER_VERIFIED);
	}

	@Test
	@Order(9)
	public void assert_test_user_verified() {
		log.info("AuthorizationColtrollerTest | assert_test_user_verified | Checando se o email do usuário está verificado");
		assertTrue(user.isVerified());
	}
	
	@Test
	@Order(10)
	public void should_send_password_reset_token() {
		log.info("AuthorizationColtrollerTest | should_send_password_reset_token | Enviando email para recuperação de senha");
		Response response = post("/authorization/recovery/" + user.getEmail(), authHeader);
		assertEquals(response.getStatusCode(), OK.value());
	}
	
	@Test
	@Order(11)
	public void should_reset_password() {
		log.info("AuthorizationColtrollerTest | should_reset_password | Deve resetar a senha do usuario {}", user.getEmail());
		RecoveryPassword recovery = recoveryRepository.findByEmail(user.getEmail());
		ResetPassword password = new ResetPassword(user.getEmail(), newPassword, newPassword);
		Response response = post("/authorization/reset/" + recovery.getToken(), password, authHeader);
		assertEquals(response.getStatusCode(), OK.value());
	}
}
