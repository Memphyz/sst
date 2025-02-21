package com.healthrib.enums.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValidationMessagesType {
	
	NOT_NULL("NOT_NULL_VALIDATION"),
	NOT_BLANK("NOT_BLANK_VALIDATION"),
	SIZE("SIZE_VALIDATION"),
	NOT_EMPTY("NOT_EMPTY_VALIDATION");
	
	private String name;
}
