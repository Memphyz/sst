package com.healthrib.annotations.implementations;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.healthrib.annotations.Email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidatorImpl implements ConstraintValidator<Email, String> {

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if(email == null || !StringUtils.hasLength(email)) {
			return false;
		}
		return Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email).matches();
	}

}
