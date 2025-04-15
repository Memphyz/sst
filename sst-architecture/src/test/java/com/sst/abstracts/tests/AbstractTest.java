package com.sst.abstracts.tests;

import static com.sst.enums.user.permission.UserPermissionType.TEST;
import static com.sst.utils.RestAssuredUtil.delete;
import static com.sst.utils.RestAssuredUtil.get;
import static com.sst.utils.RestAssuredUtil.post;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sst.abstracts.controller.AbstractController;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTest<Controller extends AbstractController<?, ?, ?>> {
	
	protected static final Map<String, Object> context = new HashMap<>();
	
	@Value("${account-test.admin.email}")
	protected String adminEmail;

	@Value("${account-test.admin.password}")
	protected String adminPassword;
	
	@Autowired
    protected Controller controller;
	
	public abstract <T> T getTestId();
	
	public String getEmail() {
		@SuppressWarnings("unchecked")
		Map<String, String> credentials = (Map<String, String>) context.get("credentials");
		return credentials.get("email");
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getAuth() {
		return (Map<String, String>) context.get("auth");
	}
	
	protected String getControllerBasePath() {
        RequestMapping requestMapping = controller.getClass().getAnnotation(RequestMapping.class);
        return requestMapping != null ? requestMapping.value()[0] : "";
    }

	@BeforeAll
	public void beforeStart() {
		log.info("AbstractTest | beforeStart | Criando usuário com dados mockados para testes");
		context.put("credentials", Map.of(
	            "name", "JUnit",
	            "password", "junit@2025",
	            "email", "junit@sstbeforeallsignup.com",
	            "roles", List.of(TEST)
	        ));
		log.info("AbstractTest | beforeStart | Obtendo token de ADMINISTRADOR para realizar alterações no banco");
		Response response = post("/authorization/signin", Map.of(
				"email", adminEmail,
				"password", adminPassword
		        ));
		String token = (String) response.getBody().as(Map.class).get("accessToken");
		context.put("auth", Map.of(
				"Authorization", "Bearer " + token
	        ));
		assertNotNull(token);
		log.info("AbstractTest | beforeStart | Token obtido com sucesso!");
		log.info("AbstractTest | beforeStart | Verificando se o usuário de testes existe");
		Response userResponse = get("/api/v1/user/email/" + getEmail(), getAuth());
		if (userResponse.getStatusCode() == FORBIDDEN.value()) {
			log.info("AbstractTest | beforeStart | Usuário {} não existe na base de dados, tudo certo.",
					getEmail());
			return;
		}
		log.info("AbstractTest | beforeStart | Usuário {} existe na base de dados, realizando limpeza.",
				getEmail());
		delete("/api/v1/user/email/" + getEmail(), getAuth());
		log.info("AbstractTest | beforeStart | Limpeza concluída");
	}
	
	@Test
	public void should_save_by_id() {
		Response model = get(this.getControllerBasePath() + "/" + this.getTestId(), this.getAuth());
		assertNotNull(model);
	}

	@AfterAll
	public void finish_tests() {
		log.info("AuthorizationColtrollerTest | finish_tests | Deletando usuario {}", getEmail());
		delete("/api/v1/user/email/" + getEmail());
		log.info("AuthorizationColtrollerTest | finish_tests | Finalizando testes...");
	}
}
