package com.sst.abstracts.tests;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;
import static java.util.stream.Collectors.toList;
import static org.instancio.settings.Keys.BEAN_VALIDATION_ENABLED;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.instancio.Instancio;
import org.instancio.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;

import com.mifmif.common.regex.Generex;
import com.sst.abstracts.controller.AbstractController;
import com.sst.abstracts.model.AbstractModelResource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Pattern;

public abstract class AbstractMockTest<Controller extends AbstractController<?, ?, ?>> {

	@Autowired
	protected Controller controller;

	private Object mockResource;
	private List<Object> mockResources = new ArrayList<Object>();
	
	protected final static Integer MOCK_RESOURCES_LIST_QUANTITY = 20;

	@SuppressWarnings("unchecked")
	protected <Resource extends AbstractModelResource<?, ?, ?>> Resource getResource(Class<Resource> clazz) {
		return (Resource) mockResource;
	}
	
	@SuppressWarnings("unchecked")
	protected <Resource extends AbstractModelResource<?, ?, ?>> List<Resource> getResources(Class<Resource> clazz) {
		return mockResources.stream().map(resource -> (Resource) resource).collect(toList());
	}
	
	@SuppressWarnings("unchecked")
	protected <Resource extends AbstractModelResource<?, ?, ?>> List<Resource> getResources() {
		return mockResources.stream().map(resource -> (Resource) resource).collect(toList());
	}
	
	@SuppressWarnings("unchecked")
	protected <Resource extends AbstractModelResource<?, ?, ?>> Resource getResource() {
		return (Resource) mockResource;
	}
	
	protected <Resource extends AbstractModelResource<?, ?, ?>> void setResource(Resource resource) {
		mockResource = resource;
	}

	protected void setup() {
		Type[] genericTypes = getControllerGenericTypes();
		if (genericTypes.length >= 2) {
			mockResource = createMockInstance(genericTypes[1]);
			for (int i = 0; i < MOCK_RESOURCES_LIST_QUANTITY; i++) {
				mockResources.add(createMockInstance(genericTypes[1]));
			}
		}
	}

	private Type[] getControllerGenericTypes() {
		Type genericSuperclass = controller.getClass().getGenericSuperclass();

		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) genericSuperclass;
			if (pt.getActualTypeArguments().length > 0) {
				return pt.getActualTypeArguments();
			}
		}

		for (Type genericInterface : controller.getClass().getGenericInterfaces()) {
			if (genericInterface instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) genericInterface;
				if (pt.getRawType().equals(AbstractController.class)) {
					return pt.getActualTypeArguments();
				}
			}
		}

		throw new IllegalStateException("Error while attempt to extract generic types from controller!");
	}
	
	protected <T> T createMockInstance() {
		Type[] genericTypes = getControllerGenericTypes();
		return createMockInstance(genericTypes[1]);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T createMockInstance(Type type) {
		try {
			Class<T> clazz;
			if (type instanceof Class) {
				clazz = (Class<T>) type;
			} else if (type instanceof ParameterizedType) {
				clazz = (Class<T>) ((ParameterizedType) type).getRawType();
			} else {
				throw new IllegalArgumentException("Mock type unsuportted: " + type);
			}
			T instance = Instancio.of(clazz).withSettings(Settings.create().set(BEAN_VALIDATION_ENABLED, true))
					.create();
			generateAllPatterns(instance);
			Validator validator = buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<T>> violations = validator.validate(instance);
			if(!violations.isEmpty()) {
				throw new IllegalStateException("Object created does not respect jakarta validations");
			}
			return instance;
		} catch (Exception e) {
			throw new RuntimeException("Failed to create a mock for: " + type, e);
		}
	}
	
	private <T> T generateAllPatterns(T instance) {
		for (Field field : instance.getClass().getDeclaredFields()) {
	        field.setAccessible(true);
	        Pattern pattern = field.getAnnotation(Pattern.class);
	        if (pattern != null && field.getType().equals(String.class)) {
	            String validValue = new Generex(pattern.regexp()).random();
	            try {
					field.set(instance, validValue);
				} catch (Exception e) {
					throw new RuntimeException("Error on create a valid regex to " + field.getName());
				}
	        }
	    }
		return instance;
	}

}
