package com.healthrib.resources;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import java.util.List;

import com.healthrib.enums.user.permission.UserPermissionType;

import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
	
	@Email
	@NotNull
	@Size(min = 5, max = 255)
	@SchemaProperty(name = "User credential email also used as ID")
	private String email;
	
	@NotNull
	@NotBlank
	@Size(min = 6, max = 255)
	@SchemaProperty(name ="User credential password")
	private String password;

	
	@Override
	public String toString() {
		return reflectionToString(this, JSON_STYLE);
	}
}
