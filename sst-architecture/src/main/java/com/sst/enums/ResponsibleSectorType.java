package com.sst.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponsibleSectorType {
	SAFETY("SAFETY"),
    PRODUCTION("PRODUCTION"),
    MAINTENANCE("MAINTENANCE"),
    HR("HR"),
    ADMINISTRATION("ADMINISTRATION"),
    WAREHOUSE("WAREHOUSE"),
    QUALITY("QUALITY"),
    OCCUPATIONAL_HEALTH("OCCUPATIONAL_HEALTH"),
    CONTRACTORS("CONTRACTORS"),
    EMERGENCY_TEAM("EMERGENCY_TEAM");
    
	private String code;
}
