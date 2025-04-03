package com.sst.exceptions;

import static com.sst.enums.ValidationMessagesType.RESOURCE_NOT_FOUND;

import com.sst.abstracts.model.AbstractExceptionModel;
import com.sst.enums.ValidationMessagesType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceNotFound extends AbstractExceptionModel {
	
	private static final long serialVersionUID = 1L;
	
	public ResourceNotFound(ValidationMessagesType message) {
		super(message);
	}
	
	public ResourceNotFound() {
		super(RESOURCE_NOT_FOUND);
	}

}
