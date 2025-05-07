package com.sst.model.sector;

import static com.sst.constants.RegexConstants.PHONE_REGEX;

import java.time.LocalTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import com.sst.abstracts.model.auditable.AbstractModelAuditable;
import com.sst.model.responsible.Responsible;
import com.sst.type.status.StatusType;

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
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sector")
@Schema(description = "Sector model")
@EqualsAndHashCode(callSuper = true)
public class Sector extends AbstractModelAuditable<String> {

	private static final long serialVersionUID = 1L;

	@Email
	@NotNull
	@MongoId
	@Size(min = 5, max = 5)
	@Indexed(unique = true)
	@Schema(description = "Sector code also used as ID")
	private String code;

	@NotNull
	@NotBlank
	@Size(min = 5, max = 60)
	@Schema(description = "Name of Sector")
	private String name;

	@NotNull
	@NotBlank
	@Size(min = 10, max = 255)
	@Schema(description = "Sector Description, ex: First floor at building 1 room 45")
	private String description;

	@DBRef(db = "responsible")
	@NotNull
	@Schema(description = "A person responsible for this sector")
	private Responsible responsible;

	@DateTimeFormat(pattern = "hh:mm")
	@NotNull
	@Schema(description = "Sector opening hours")
	private LocalTime businessHours;

	@Pattern(regexp = PHONE_REGEX)
	@NotNull
	@NotBlank
	@Schema(description = "Sector contact number")
	private String phone;

	@NotNull
	@Schema(description = "Sector status")
	private StatusType status;

	@Override
	public String getId() {
		return this.code;
	}

	@Override
	public void setId(String id) {
		this.code = id;
	}
	
	
}
