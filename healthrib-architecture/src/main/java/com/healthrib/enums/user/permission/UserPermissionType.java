package com.healthrib.enums.user.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserPermissionType {
	
	ADMIN("ADMIN"),
	MANAGER("MANAGER"),
	COWORKER("COWORKER"),
	AUDITOR("AUDITOR"),
	DOCTOR("DOCTOR"),
	OUTSOURCED("OUTSOURCED");

	private String name;
	
}
