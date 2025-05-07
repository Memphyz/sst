package com.sst.api.v1.test.iar;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.sst.TestConfig;
import com.sst.abstracts.tests.AbstractTest;
import com.sst.controller.iar.IARController;
import com.sst.resources.iar.IARResource;

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
		return resource;
	}
	
	

}
