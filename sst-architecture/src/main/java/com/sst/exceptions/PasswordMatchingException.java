package com.sst.exceptions;

import static com.sst.enums.ValidationMessagesType.PASSWORD_CONFLICT;

import com.sst.abstracts.model.AbstractExceptionModel;

public class PasswordMatchingException extends AbstractExceptionModel {

	private static final long serialVersionUID = 1L;

	public PasswordMatchingException() {
		super(PASSWORD_CONFLICT);
	}

}
