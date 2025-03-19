package com.sst.abstracts.model;

import com.sst.enums.ValidationMessagesType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractExceptionModel extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;

	public AbstractExceptionModel(ValidationMessagesType message) {
		this.message = message.getMessage();
	}

}
