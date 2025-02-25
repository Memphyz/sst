package com.healthrib.model.user;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.healthrib.abstracts.model.auditable.AbstractModelAuditable;
import com.healthrib.annotations.Email;
import com.healthrib.annotations.NotBlank;
import com.healthrib.annotations.NotNull;
import com.healthrib.annotations.Size;
import com.healthrib.enums.user.permission.UserPermissionType;
import com.healthrib.type.status.StatusType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Document
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractModelAuditable<String> {

	private static final long serialVersionUID = 1L;

	@NotNull
	@NotBlank
	@Size(min = 3, max = 60)
	private String firstname;

	@NotNull
	@NotBlank
	@Size(min = 3, max = 20)
	private String name;
	
	@NotNull
	@NotBlank
	@Size(min = 3, max = 15)
	private String username;

	@NotNull
	@NotBlank
	@Size(min = 6, max = 255)
	private String password;

	@Email
	private String email;
	
	@NotNull
	private boolean verified;

	@NotNull
	private StatusType status;

	@NotNull
	private List<UserPermissionType> roles;

}
