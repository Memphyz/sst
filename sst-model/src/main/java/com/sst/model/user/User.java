package com.sst.model.user;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sst.abstracts.model.auditable.AbstractModelAuditable;
import com.sst.enums.user.permission.UserPermissionType;
import com.sst.type.status.StatusType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "User model")
@Document
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractModelAuditable<String> implements UserDetails {

	private static final long serialVersionUID = 1L;

	@NotNull
	@NotBlank
	@Size(min = 3, max = 120)
	@Schema(description = "User name")
	private String name;
	
	@NotNull
	@NotBlank
	@Size(min = 6, max = 255)
	@Schema(description ="User password")
	private String password;

	@Email
	@NotNull
	@MongoId
	@Size(min = 5, max = 255)
	@Indexed(unique=true)
	@Schema(description = "User email also used as ID")
	private String email;
	
	@NotNull
	@Schema(description = "A flag that verify if user confirmed your email")
	private boolean verified;
	
	@NotNull
	@Valid
	@Schema(description = "User status (ACTIVE or INACTIVE)")
	private StatusType status;
	
	@NotNull
	@Valid
	@NotEmpty
	@Schema(description = "User roles access")
	private List<UserPermissionType> roles;
	
	@JsonIgnore
	private Boolean accountNonExpired;

	@JsonIgnore
	private Boolean accountNonLocked;

	@JsonIgnore
	private Boolean credentialsNonExpired;

	@JsonIgnore
	private Boolean enabled;

	@Override
	public Collection<UserPermissionType> getAuthorities() {
		return this.roles;
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return this.getEmail();
	}

}
