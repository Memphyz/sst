package com.sst.exceptions;

import com.sst.abstracts.model.AbstractExceptionModel;
import com.sst.enums.ValidationMessagesType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserAlreadyExistsException extends AbstractExceptionModel {
	
	private static final long serialVersionUID = 1L;
	
	public UserAlreadyExistsException(ValidationMessagesType message) {
		super(message);
	}

}
