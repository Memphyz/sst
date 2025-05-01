package com.sst.model.hazardous.agent;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.sst.abstracts.model.AbstractModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "hazardous_agent")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Hazardous Agent model")
@EqualsAndHashCode(callSuper = true)
public class HazardousAgent extends AbstractModel<String> {
	
	private static final long serialVersionUID = 1L;

	@Email
	@NotNull
	@NotBlank
	@MongoId
	@Size(min = 7, max = 15)
	@Indexed(unique = true)
	@Schema(description = "Code as id")
	@Pattern(regexp = "^(\\d+)$")
	private String code;
	
	@NotNull
	@NotBlank
	@Size(min = 5, max = 120)
	private String name;

	@Override
	public String getId() {
		return this.code;
	}

	@Override
	public void setId(String id) {
		this.code = id;
	}
	
}
