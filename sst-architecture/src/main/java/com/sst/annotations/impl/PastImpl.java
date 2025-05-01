package com.sst.annotations.impl;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;

import com.sst.annotations.Past;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PastImpl implements ConstraintValidator<Past, LocalDateTime> {

	@Override
	public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		return value.isBefore(now());
	}

}
