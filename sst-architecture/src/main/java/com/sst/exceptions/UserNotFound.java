package com.sst.exceptions;

import static com.sst.enums.ValidationMessagesType.USER_NOT_FOUND;

import com.sst.abstracts.model.AbstractExceptionModel;
import com.sst.enums.ValidationMessagesType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UserNotFound extends AbstractExceptionModel {
	private static final long serialVersionUID = 1L;
	
	public UserNotFound(ValidationMessagesType message) {
		super(message);
	}
	
	public UserNotFound() {
		super(USER_NOT_FOUND);
	}
}
