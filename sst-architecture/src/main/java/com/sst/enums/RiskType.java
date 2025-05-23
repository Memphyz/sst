package com.sst.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RiskType {
	
	PHYSICAL("Physical"),
	CHEMICAL("Chemical"),
	BIOLOGICAL("Biological"),
	ERGONOMIC("Ergonomic"),
	MECHANICAL("Mechanical");
	
	private String risk;
}
