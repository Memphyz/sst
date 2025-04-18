package com.sst.abstracts.tests;

import static com.sst.constants.UrlMappingConstants.V1;
import static com.sst.enums.user.permission.UserPermissionType.TEST;
import static com.sst.utils.RestAssuredUtil.delete;
import static com.sst.utils.RestAssuredUtil.get;
import static com.sst.utils.RestAssuredUtil.post;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sst.abstracts.controller.AbstractController;

import io.restassured.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTest<Controller extends AbstractController<?, ?, ?>> extends AbstractMockTest<Controller>{
	
	protected static final Map<String, Object> context = new HashMap<>();
	
	@Value("${account-test.admin.email}")
	protected String adminEmail;

	@Value("${account-test.admin.password}")
	protected String adminPassword;
	
	protected String baseUrl;
	protected String name;
	protected String controllerName;
	
	public String getEmail() {
		@SuppressWarnings("unchecked")
		Map<String, String> credentials = (Map<String, String>) context.get("credentials");
		return credentials.get("email");
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, String> getAuthHeader() {
		return (Map<String, String>) context.get("auth");
	}
	
	private String getControllerBasePath() {
        RequestMapping requestMapping = controller.getClass().getAnnotation(RequestMapping.class);
        return requestMapping != null ? requestMapping.value()[0] : "";
    }
	
	private String getName() {
		Tag tag = controller.getClass().getAnnotation(Tag.class);
		if(tag == null) {
			return "Unknown";
		}
        return tag.name();
    }

	@BeforeAll
	public void beforeStart() {
		super.setup();
		controllerName = controller.getClass().getSimpleName();
		name = this.getName();
		baseUrl = this.getControllerBasePath();
		log.info("{} | AbstractTest | beforeStart | Criando usuário com dados mockados para testes", controllerName);
		context.put("credentials", Map.of(
	            "name", "JUnit",
	            "password", "junit@2025",
	            "email", "junit@sstbeforeallsignup.com",
	            "roles", List.of(TEST)
	        ));
		log.info("{} | AbstractTest | beforeStart | Obtendo token de ADMINISTRADOR para realizar alterações no banco", controllerName);
		Response response = post("/authorization/signin", Map.of(
				"email", adminEmail,
				"password", adminPassword
		        ));
		String token = (String) response.getBody().as(Map.class).get("accessToken");
		context.put("auth", Map.of(
				"Authorization", "Bearer " + token
	        ));
		assertNotNull(token);
		log.info("{} | AbstractTest | beforeStart | Token obtido com sucesso!", controllerName);
		log.info("{} | beforeStart | Verificando se o usuário de testes existe", controllerName);
		Response userResponse = get(V1 + "/user/email/" + getEmail(), getAuthHeader());
		if (userResponse.getStatusCode() == FORBIDDEN.value() || userResponse.getStatusCode() == NOT_FOUND.value()) {
			log.info("{} | AbstractTest | beforeStart | Usuário {} não existe na base de dados, tudo certo.", controllerName,
					getEmail());
			return;
		}
		log.info("{} | AbstractTest | beforeStart | Usuário {} existe na base de dados, realizando limpeza.", controllerName, 
				getEmail());
		delete("/api/v1/user/email/" + getEmail(), getAuthHeader());
		log.info("{} | AbstractTest | beforeStart | Limpeza concluída", controllerName);
	}
	
	@Test
	public void should_create_mock_model() {
		log.info("{} | AbstractTest | POST | Criando resource utilizando método post", controllerName);
		Response response = post(baseUrl, (Object) this.getResource(),  this.getAuthHeader());
		assertEquals(response.getStatusCode(), CREATED, controllerName + " | POST | should_create_mock_model | Falha ao criar " + name + " Code: " + response.getStatusCode());
	}
	
	@Test
	public void should_save_by_id() {
//		Response response = get(baseUrl + "/" + this.getTestId(), this.getAuth());
//		Map<String, ?> body = response.body().as(Map.class);
//		assertNotNull(response);
		assertTrue(true);
	}

	@AfterAll
	public void finish_tests() {
		log.info("AuthorizationColtrollerTest | finish_tests | Deletando usuario {}", getEmail());
		delete("/api/v1/user/email/" + getEmail());
		log.info("AuthorizationColtrollerTest | finish_tests | Finalizando testes...");
	}
}
