package com.sst.api.v1.test.iar;

import static com.sst.enums.InvalidEntityMessageType.DIRTY;
import static com.sst.enums.InvalidEntityMessageType.MISSING;
import static com.sst.enums.RiskType.CHEMICAL;
import static com.sst.utils.RestAssuredUtil.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.http.HttpStatus.CREATED;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.sst.TestConfig;
import com.sst.abstracts.tests.AbstractTest;
import com.sst.controller.iar.IARController;
import com.sst.exceptions.ExceptionResponse;
import com.sst.resources.iar.IARResource;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestConfig.class)
@TestInstance(PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class IARTest extends AbstractTest<IARController>{
	
	@Override
	public IARResource fixMockCreation(Object mock) {
		IARResource resource = (IARResource) mock;
		resource.getSector().setPhone("+5511999999999");
		resource.setHazardousAgentCode("000");
		resource.setExistingControlMeasures("stringstri,stringstri,stringstri,stringstri");
		return resource;
	}
	
	@Test
	public void should_validate_dirty_word() {
		log.info("IARTest | should_validate_dirty_word | Deve validar palavrões no campo ExistingControlMeasures");
		IARResource resource = createMockInstance();
		resource.setExistingControlMeasures("fuck this fucking test");
		Response response = post(baseUrl, resource, getAuthHeader());
		ExceptionResponse exception = response.body().as(ExceptionResponse.class);
		assertEquals(exception.getMessage(), DIRTY.get("ExistingControlMeasures"));
	}
	
	@Test
	public void should_validate_missing_hazardous_agent_code() {
		log.info("IARTest | should_validate_missing_hazardous_agent_code | Deve validar se a exception esta sendo disparada ao informar agente quimico sem codigo");
		IARResource resource = createMockInstance();
		resource.setRiskType(CHEMICAL);
		resource.setHazardousAgentCode(null);
		Response response = post(baseUrl, resource, getAuthHeader());
		ExceptionResponse exception = response.body().as(ExceptionResponse.class);
		assertEquals(exception.getMessage(), MISSING.get("HazardousAgentCode"));
	}
	
	@Test
	public void should_validate_hazardous_agent_code() {
		log.info("IARTest | should_validate_hazardous_agent_code | Deve validar se o documento foi salvo caso o tipo de risco for quimico");
		IARResource resource = createMockInstance();
		resource.setRiskType(CHEMICAL);
		resource.setHazardousAgentCode("00000000");
		Response response = post(baseUrl, resource, getAuthHeader());
		assertEquals(response.getStatusCode(), CREATED.value());
	}

}
