package com.sst.annotations.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.sst.annotations.Future;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FutureImpl implements ConstraintValidator<Future, Object> {
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value == null) {
			return true;
		}
		if(value instanceof LocalDateTime) {
			LocalDateTime time = (LocalDateTime) value;
			return time.isAfter(LocalDateTime.now()) || time.isEqual(LocalDateTime.now());
		}
		
		if(value instanceof LocalDate) {
			LocalDate time = (LocalDate) value;
			return time.isAfter(LocalDate.now()) || time.isEqual(LocalDate.now());
		}
		if(value instanceof LocalTime) {
			LocalTime time = (LocalTime) value;
			return time.isAfter(LocalTime.now());
		}
		return false;
	}

}
