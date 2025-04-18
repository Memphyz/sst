package com.sst.abstracts.tests;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;

import com.sst.abstracts.controller.AbstractController;

public abstract class AbstractMockTest<Controller extends AbstractController<?, ?, ?>> {

	@Autowired
	protected Controller controller;

	private Object mockModel;
	private Object mockResource;
	
	@SuppressWarnings("unchecked")
	protected <M> M getModel() {
		return (M) mockModel;
	}

	
	@SuppressWarnings("unchecked")
	protected <DTO> DTO getResource() {
		return (DTO) mockResource;
	}
	
	protected void setup() {
		Type[] genericTypes = getControllerGenericTypes();
		if (genericTypes.length >= 2) {
			mockModel = createMockInstance(genericTypes[0]);
			mockResource = createMockInstance(genericTypes[1]);
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
			return Instancio.create(clazz);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create a mock for: " + type, e);
		}
	}

}
