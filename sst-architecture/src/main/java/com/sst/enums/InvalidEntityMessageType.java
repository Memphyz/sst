package com.sst.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InvalidEntityMessageType {
	
	INSUFICIENT("INSUFICIENT"),
	DIRTY("DIRTY"),
	MISSING("MISSING");
	
	private String type;
	
	public String get(String field) {
		return type + "_" + field;
	}
}
