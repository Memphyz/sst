package com.sst.enums.user.permission;

import static java.util.Arrays.asList;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserPermissionType implements GrantedAuthority {
	
	MANAGER("MANAGER"),
	COWORKER("COWORKER"),
	AUDITOR("AUDITOR"),
	DOCTOR("DOCTOR"),
	SECUTIRY_MANAGER("SECUTIRY_MANAGER"),
	BUSINESS("BUSINESS"),
	ADMINISTRATOR("ADMINISTRATOR"),
	OUTSOURCED("OUTSOURCED");

	private String name;

	@Override
	public String getAuthority() {
		return this.name;
	}
	
	public static final List<UserPermissionType> ALL = asList(MANAGER, COWORKER, AUDITOR, DOCTOR, SECUTIRY_MANAGER, BUSINESS, ADMINISTRATOR, OUTSOURCED);
	
}
