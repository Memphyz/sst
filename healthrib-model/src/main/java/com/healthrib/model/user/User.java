package com.healthrib.model.user;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;

import com.healthrib.abstracts.model.auditable.AbstractModelAuditable;
import com.healthrib.enums.user.permission.UserPermissionType;
import com.healthrib.type.status.StatusType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Document
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractModelAuditable<String> implements UserDetails {

	private static final long serialVersionUID = 1L;

	@NotNull
	@NotBlank
	@Size(min = 3, max = 120)
	private String name;
	
	@NotNull
	@NotBlank
	@Size(min = 6, max = 255)
	private String password;

	@Email
	@NotNull
	@Size(min = 5, max = 255)
	private String email;
	
	@NotNull
	private boolean verified;
	
	@NotNull
	@Valid
	private StatusType status;

	@NotNull
	@Valid
	private List<UserPermissionType> roles;

	@Override
	public Collection<UserPermissionType> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getUsername() {
		return this.getEmail();
	}

}
