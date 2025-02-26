package com.healthrib.resources;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
	
	@NotNull
	@NotEmpty
	private String email;
	
	@NotNull
	@NotEmpty
	private String password;
	
	@Override
	public String toString() {
		return reflectionToString(JSON_STYLE);
	}

}
