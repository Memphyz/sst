package com.sst.exceptions;

import static com.sst.enums.ValidationMessagesType.USER_ROLE_PERMISSION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.web.bind.annotation.ResponseStatus;

import com.sst.enums.ValidationMessagesType;

@ResponseStatus(FORBIDDEN)
public class UserPermissionDenied extends org.springframework.security.core.AuthenticationException {
	
	private static final long serialVersionUID = 1L;
	
	public UserPermissionDenied() {
		super(USER_ROLE_PERMISSION.getMessage());
	}

	public UserPermissionDenied(ValidationMessagesType validation) {
		super(validation.getMessage());
	}

}
