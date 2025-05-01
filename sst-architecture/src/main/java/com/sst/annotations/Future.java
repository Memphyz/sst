package com.sst.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.auth0.jwt.interfaces.Payload;
import com.sst.annotations.impl.FutureImpl;

import jakarta.validation.Constraint;

/**
 * Validate if a value is after or equals than actual date.
 * 
 * @author Lucas Ribeiro
 * @version 1.0
 * */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = FutureImpl.class)
public @interface Future {
	String message() default "The value should be greater than now";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
