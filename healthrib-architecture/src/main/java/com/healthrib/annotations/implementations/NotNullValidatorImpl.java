package com.healthrib.annotations.implementations;

import com.healthrib.annotations.NotNull;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNullValidatorImpl implements ConstraintValidator<NotNull, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null;
	}

}
