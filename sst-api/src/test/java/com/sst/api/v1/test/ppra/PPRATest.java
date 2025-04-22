package com.sst.api.v1.test.ppra;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.sst.TestConfig;
import com.sst.abstracts.tests.AbstractTest;
import com.sst.controller.ppra.PPRAController;
import com.sst.resources.ppra.PPRAResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestConfig.class)
@TestInstance(PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class PPRATest extends AbstractTest<PPRAController> {
	
	@Override
	@SuppressWarnings("unchecked")
	protected String getId() {
		return this.getResource(PPRAResource.class).getCode();
	}


}
