package com.sst.abstracts.tests;

import static com.sst.utils.RestAssuredUtil.delete;
import static com.sst.utils.RestAssuredUtil.get;
import static com.sst.utils.RestAssuredUtil.post;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sst.abstracts.controller.AbstractController;
import com.sst.abstracts.model.AbstractModelResource;

import io.restassured.response.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestInstance(PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public abstract class AbstractTest<Controller extends AbstractController<?, ?, ?>> extends AbstractMockTest<Controller>{
	
	protected static final Map<String, Object> context = new HashMap<>();
	
	@Value("${account-test.admin.email}")
	protected String adminEmail;

	@Value("${account-test.admin.password}")
	protected String adminPassword;
	
	protected String baseUrl;
	protected String name;
	protected String controllerName;
	
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
	
	protected <R extends AbstractModelResource<?, ?, ?>> Object getId(R resource) {
		return resource.getId();
	}
	
	private <Resource extends AbstractModelResource<?, ?, ?>> Object getId() {
		return getId(getResource());
	}

	@BeforeAll
	public void beforeStart() {
		super.setup();
		controllerName = controller.getClass().getSimpleName();
		name = this.getName();
		baseUrl = this.getControllerBasePath();
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
	}
	
	@Test
	@Order(1)
	public void should_create_mock_model() {
		log.info("{} | AbstractTest | POST | Criando resource utilizando método post", controllerName);
		Response response = post(baseUrl, (Object) this.getResource(),  this.getAuthHeader());
		assertEquals(response.getStatusCode(), CREATED.value(), controllerName + " | POST | should_create_mock_model | Falha ao criar " + name + " Code: " + response.getStatusCode());
	}
	
	@Test
	@Order(2)
	public void should_get_by_id() {
		Response response = get(baseUrl + "/" + getId(), this.getAuthHeader());
		Map<?, ?> body = response.body().as(Map.class);
		assertEquals(response.getStatusCode(), OK.value());
		assertNotNull(body.get("id"));
	}
	
	@Test
	@Order(3)
	public void should_save_all_mocks() {
		Response response = post(baseUrl + "/all", getResources(), getAuthHeader());
		assertEquals(response.getStatusCode(), OK.value());
		assertEquals(response.body().as(List.class).size(), MOCK_RESOURCES_LIST_QUANTITY);
	}
	
	@Test
	@Order(4)
	public void should_get_all_paginated_mocks() {
		Response response = get(baseUrl, Map.of(
				"size", "10",
				"page", "0"
				), getAuthHeader());
		@SuppressWarnings("unchecked")
		List<Object> entities = response.body().as(List.class);
		String contentRange = response.header("X-Content-Range");
		String contentPages = response.header("X-Content-Pages");
		assertTrue(isNumeric(contentPages));
		assertTrue(contentRange.matches("^(\\d+)/(\\d+)$"));
		assertDoesNotThrow(() -> parseInt(contentPages));
		assertDoesNotThrow(() -> parseInt(contentRange.split("/")[0]));
		assertDoesNotThrow(() -> parseInt(contentRange.split("/")[1]));
		assertEquals(response.getStatusCode(), OK.value());
		assertEquals(entities.size(), 10);
	}
	
	@Test
	@Order(5)
	public void should_delete_all_mocks() {
		Response response = delete(baseUrl + "/all", getResources(), getAuthHeader());
		assertEquals(response.getStatusCode(), OK.value());
	}
	
	@Test
	@Order(6)
	public void should_delete_all_mocks_by_ids() {
		should_save_all_mocks();
		String ids = StringUtils.join(",", getResources().stream().map(resource -> getId(resource)).collect(toList()));
		Response response = delete(baseUrl + "/all", Map.of("ids", ids), getAuthHeader());
		assertEquals(response.getStatusCode(), OK.value());
	}
	
	@Test
	@Order(7)
	@SuppressWarnings("unchecked")
	public void should_update_document() {
		@SuppressWarnings("rawtypes")
		AbstractModelResource newInstance = createMockInstance();
		newInstance.setId(this.getResource().getId());
		assertNotEquals(this.getResource().toString(), newInstance.toString());
		Response response = post(baseUrl, newInstance, this.getAuthHeader());
		String bodyRaw = response.body().asPrettyString();
		Map<?, ?> body = response.body().as(Map.class);
		assertEquals(response.getStatusCode(), OK.value());
		assertNotNull(body.get("id"));
		this.setResource(newInstance);
		Response updated = get(baseUrl + "/" + this.getId(), this.getAuthHeader());
		Object newBodyRaw = updated.body().asPrettyString();
		assertNotEquals(bodyRaw, newBodyRaw);
	}

	@AfterAll
	public void finish_tests() {
		should_delete_reosurce(getResource());
		should_delete_all_mocks();
		log.info("{} | AuthorizationColtrollerTest | finish_tests | Finalizando testes...", controllerName);
	}

	private void should_delete_reosurce(AbstractModelResource<?, ?, ?> resource) {
		log.info("{} | AuthorizationColtrollerTest | finish_tests | Deletando documento {}",controllerName,  this.getId());
		Response response = delete(baseUrl + "/" + this.getId(resource), this.getAuthHeader());
		assertEquals(response.getStatusCode(), OK.value());
	}

}
