package com.healthrib.enums.user.permission;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserPermissionType implements GrantedAuthority {
	
	ADMIN("ADMIN"),
	MANAGER("MANAGER"),
	COWORKER("COWORKER"),
	AUDITOR("AUDITOR"),
	DOCTOR("DOCTOR"),
	OUTSOURCED("OUTSOURCED");

	private String name;

	@Override
	public String getAuthority() {
		return this.name;
	}
	
}
