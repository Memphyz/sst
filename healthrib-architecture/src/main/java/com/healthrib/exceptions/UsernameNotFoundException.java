package com.healthrib.exceptions;

import com.healthrib.enums.ValidationMessagesType;

public class UsernameNotFoundException extends org.springframework.security.core.userdetails.UsernameNotFoundException{

	private static final long serialVersionUID = 1L;

	public UsernameNotFoundException(ValidationMessagesType message) {
		super(message.getMessage());
	}

}
