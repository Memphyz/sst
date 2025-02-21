package com.healthrib.abstracts.validators;

import com.healthrib.validations.NotNull;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNullValidatorImpl implements ConstraintValidator<NotNull, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null;
	}

}
