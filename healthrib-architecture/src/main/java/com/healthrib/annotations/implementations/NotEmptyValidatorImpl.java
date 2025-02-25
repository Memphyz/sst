package com.healthrib.annotations.implementations;

import org.springframework.util.StringUtils;

import com.healthrib.annotations.NotEmpty;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyValidatorImpl implements ConstraintValidator<NotEmpty, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return StringUtils.hasLength(value);
	}

}
