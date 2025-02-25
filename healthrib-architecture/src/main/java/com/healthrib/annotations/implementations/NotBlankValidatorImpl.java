package com.healthrib.annotations.implementations;

import static org.springframework.util.StringUtils.hasText;

import com.healthrib.annotations.NotBlank;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankValidatorImpl implements ConstraintValidator<NotBlank, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return hasText(value);
	}

}
