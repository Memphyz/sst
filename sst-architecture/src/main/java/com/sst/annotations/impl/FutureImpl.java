package com.sst.annotations.impl;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;

import com.sst.annotations.Future;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FutureImpl implements ConstraintValidator<Future, LocalDateTime> {
	
	@Override
	public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		return value.isAfter(now()) || value.isEqual(now());
	}

}
