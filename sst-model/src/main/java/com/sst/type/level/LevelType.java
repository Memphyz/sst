package com.sst.type.level;

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
