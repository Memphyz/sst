package com.healthrib.user;

import org.springframework.data.mongodb.core.mapping.Document;

import com.healthrib.abstracts.model.AbstractModel;
import com.healthrib.validations.NotBlank;
import com.healthrib.validations.NotNull;
import com.healthrib.validations.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Document
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractModel<String> {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@NotBlank
	@Size
	private String firstname;

}
