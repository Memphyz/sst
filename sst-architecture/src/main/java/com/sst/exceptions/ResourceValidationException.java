package com.sst.exceptions;

import static com.sst.enums.ValidationMessagesType.RESOURCE_NOT_FOUND;

import com.sst.abstracts.model.AbstractExceptionModel;
import com.sst.enums.ValidationMessagesType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceValidationException extends AbstractExceptionModel {
	
	private static final long serialVersionUID = 1L;
	
	public ResourceValidationException(ValidationMessagesType message) {
		super(message);
	}
	
	public ResourceValidationException(String message) {
		super(message);
	}
	
	public ResourceValidationException() {
		super(RESOURCE_NOT_FOUND);
	}


}
