package com.healthrib.model.user;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Document
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	private String firstname;

}
