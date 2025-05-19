package com.sst.api.v1.test.psst;

import static com.sst.enums.DisclosureMethodType.EMAIL;
import static com.sst.enums.InvalidEntityMessageType.MISSING;
import static com.sst.enums.InvalidEntityMessageType.MUST_BE_GREATER_THAN;
import static com.sst.enums.ValidationMessagesType.RESOURCE_VALIDATION;
import static com.sst.utils.RestAssuredUtil.post;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.sst.TestConfig;
import com.sst.abstracts.tests.AbstractTest;
import com.sst.controller.psst.PSSTController;
import com.sst.exceptions.ExceptionResponse;
import com.sst.resources.psst.PSSTResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestConfig.class)
@TestInstance(PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class PSSTTest extends AbstractTest<PSSTController> {
	
	@Override
	public PSSTResource fixMockCreation(Object mock) {
		PSSTResource resource = (PSSTResource) mock;
		resource.setRevisionDate(now());
		resource.setPublishDate(now());
		resource.setPrevieusVersion("1.0");
		resource.setVersion("1.1");
		return resource;
	}

	@Test
	public void should_update_without_revision_exception() {
		PSSTResource resource = this.createMockInstance();
		resource.setReasonRevision(null);
		ExceptionResponse response = post(baseUrl + "/update", resource, ExceptionResponse.class, getAuthHeader());
		assertInstanceOf(ExceptionResponse.class, response);
		assertEquals(response.getMessage(), MISSING.get("reasonRevision"));
		assertEquals(response.getType(), RESOURCE_VALIDATION.getMessage());
	}
	
	@Test
	public void should_update_without_document_exception() {
		PSSTResource resource = this.createMockInstance();
		resource.setDocument(null);
		ExceptionResponse response = post(baseUrl + "/update", resource, ExceptionResponse.class, getAuthHeader());
		assertInstanceOf(ExceptionResponse.class, response);
		assertEquals(response.getMessage(), MISSING.get("document"));
		assertEquals(response.getType(), RESOURCE_VALIDATION.getMessage());
	}
	
	@Test
	public void should_update_with_previeus_version_greater_than_actual_version_exception() {
		PSSTResource resource = this.createMockInstance();
		resource.setPrevieusVersion("1.0");
		resource.setVersion("0.1");
		ExceptionResponse response = post(baseUrl, resource, ExceptionResponse.class, getAuthHeader());
		assertInstanceOf(ExceptionResponse.class, response);
		assertEquals(response.getMessage(), MUST_BE_GREATER_THAN.get("previeusVersion", "version"));
		assertEquals(response.getType(), RESOURCE_VALIDATION.getMessage());
	}
	
	@Test
	public void should_update_disclosure_method_email_with_cc_mails_null() {
		PSSTResource resource = this.createMockInstance();
		resource.setDisclosureMethod(EMAIL);
		resource.setCcMails(null);
		ExceptionResponse response = post(baseUrl, resource, ExceptionResponse.class, getAuthHeader());
		assertInstanceOf(ExceptionResponse.class, response);
		assertEquals(response.getMessage(), "NOT_EMPTY: ccMails");
		assertEquals(response.getType(), RESOURCE_VALIDATION.getMessage());
		List<String> ccs = new ArrayList<String>();
		ccs.add("email@test.com");
		resource.setCcMails(ccs);
	}
	
	@Test
	public void should_update_disclosure_method_email_without_cc_mails() {
		PSSTResource resource = this.createMockInstance();
		resource.setDisclosureMethod(EMAIL);
		resource.setCcMails(new ArrayList<String>());
		ExceptionResponse response = post(baseUrl, resource, ExceptionResponse.class, getAuthHeader());
		assertInstanceOf(ExceptionResponse.class, response);
		assertEquals(response.getMessage(), "NOT_EMPTY: ccMails");
		assertEquals(response.getType(), RESOURCE_VALIDATION.getMessage());
		List<String> ccs = new ArrayList<String>();
		ccs.add("email@test.com");
		resource.setCcMails(ccs);
	}

}
