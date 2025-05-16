package com.sst.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LevelType {
	
	LOWER("LOWER"),
	MIDDLE("MIDDLE"),
	HIGH("HIGH");
	
	private String level;
}
