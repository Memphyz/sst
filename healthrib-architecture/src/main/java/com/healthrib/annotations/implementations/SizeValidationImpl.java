package com.healthrib.annotations.implementations;

import com.healthrib.annotations.Size;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SizeValidationImpl implements ConstraintValidator<Size, String> {
	
	private int min;
	private int max;

	@Override
	public void initialize(Size constraintAnnotation) {
		min = constraintAnnotation.min();
		max = constraintAnnotation.max();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		int length = value.length();
		return length >= min && length <= max;
	}

}
