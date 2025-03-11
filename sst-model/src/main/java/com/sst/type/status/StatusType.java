package com.sst.type.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusType {
	
	ACTIVE("ACTIVE"),
	INACTIVE("INACTIVE");
	
	private String status;

}
