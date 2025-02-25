package com.healthrib.exceptions;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.web.bind.annotation.ResponseStatus;

import com.healthrib.enums.ValidationMessagesType;

@ResponseStatus(FORBIDDEN)
public class AuthenticationException extends org.springframework.security.core.AuthenticationException {
	
	private static final long serialVersionUID = 1L;

	public AuthenticationException(ValidationMessagesType validation) {
		super(validation.getMessage());
	}

}
