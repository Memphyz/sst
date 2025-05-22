package com.sst.enums;

import static java.lang.String.join;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InvalidEntityMessageType {
	
	INSUFICIENT("INSUFICIENT"),
	DIRTY("DIRTY"),
	INVALID("INVALID"),
	MUST_BE_GREATER_THAN("MUST_BE_GREATER_THAN"),
	MUST_BE_DIFFERENT("MUST_BE_DIFFERENT"),
	MISSING("MISSING");
	
	private String type;
	
	public String get(String field) {
		return type + "_" + field;
	}
	
	public String get(String ...fields) {
		return type + "_" + join("_",fields);
	}
}
