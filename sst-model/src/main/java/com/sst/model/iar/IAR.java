package com.sst.model.iar;

import org.springframework.data.mongodb.core.mapping.Document;

import com.sst.abstracts.model.auditable.AbstractModelAuditable;
import com.sst.type.risk.RiskType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
	@Valid
	@Schema(description = "A Risk Type for a Risk Identification and Assessment")
	private RiskType riskType;

}
