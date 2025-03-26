package com.sst.type.disclosure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DisclosureMethodType {
	
	EMAIL("EMAIL"),
	INTRANET("INTRANET"),
	MEETING("MEETING"),
	OTHER("OTHER");
	
	private String code;

}
