package com.sst.model.responsible;

import static com.sst.constants.RegexConstants.ALPHA;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.sst.abstracts.model.auditable.AbstractModelAuditable;
import com.sst.annotations.Future;
import com.sst.annotations.Past;
import com.sst.enums.StatusType;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "responsible")
@Schema(description = "Responsible model")
@EqualsAndHashCode(callSuper = true)
public class Responsible extends AbstractModelAuditable<String> {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@NotBlank
	@Size(min = 3, max = 120)
	@Pattern(regexp = ALPHA)
	@Schema(description = "Name of Responsible")
	private String name;
	
	@NotNull
	@NotBlank
	@Size(min = 3, max = 60)
	private String responsibility;
	
	@NotNull
	@Future
	private LocalDateTime startAt;
	
	@Past
	private LocalDateTime endAt;
	
	@NotNull
	private StatusType status;
}
