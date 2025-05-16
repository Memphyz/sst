package com.sst.resources.user;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.sst.abstracts.resource.AbstractResource;
import com.sst.enums.user.permission.UserPermissionType;
import com.sst.model.user.User;
import com.sst.enums.StatusType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserResource extends AbstractResource<User, UserResource> {

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
	@Schema(description = "User roles access")
	private List<UserPermissionType> roles;

}
