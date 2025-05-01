package com.sst.model.iar;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sst.abstracts.model.auditable.AbstractModelAuditable;
import com.sst.model.responsible.Responsible;
import com.sst.model.sector.Sector;
import com.sst.type.level.LevelType;
import com.sst.type.risk.RiskType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "iar")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Risk Identification and Assessment model")
@EqualsAndHashCode(callSuper = true)
public class IAR extends AbstractModelAuditable<String> {

	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Schema(description = "A Risk Type for a Risk Identification and Assessment")
	private RiskType riskType;
	
	@Size(min = 1, max = 500)
	@NotNull
	@NotBlank
	private String riskDescription;
	
	@DBRef(db = "sector")
	@NotNull
	@Valid
	private Sector sector;
	
	@NotBlank
	@Pattern(regexp = "^(\\d+)$")
	private String hazardousAgentCode;
	
	@NotNull
	private LevelType severity;
	
	@NotNull
	private LevelType probability;
	
	@NotNull
	@NotBlank
	@Size(min = 10, max = 1000)
	private String existingControlMeasures;
	
	@DBRef(db = "responsible")
	@NotNull
	private Responsible responsibleControl;
	
	@NotNull
	private LocalDate spottedDate;
}
