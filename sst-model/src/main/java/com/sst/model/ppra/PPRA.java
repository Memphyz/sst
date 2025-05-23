package com.sst.model.ppra;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.sst.abstracts.model.auditable.AbstractModelAuditable;
import com.sst.model.user.User;
import com.sst.enums.DisclosureMethodType;
import com.sst.enums.ResponsibleSectorType;
import com.sst.enums.StatusType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "ppra")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PPRA model")
@EqualsAndHashCode(callSuper = true)
public class PPRA extends AbstractModelAuditable<String> {
	
	private static final long serialVersionUID = 1L;

	@NotBlank
	@NotNull
	@Size(min = 3, max = 100)
	@Schema(description = "Name of PPRA")
	private String name;
	
	@NotNull
	@MongoId
	@NotBlank
	@Indexed(unique = true)
	@Size(min = 10, max = 20)
	@Schema(description = "A unique Code of PPRA")
	private String code;
	
	@NotNull
	@NotBlank
	@Pattern(regexp = "\\d+\\.\\d+")
	@Schema(description = "A unique Version of PPRA. Every single documento should have your specific version and it not should be repeated between them")
	private String version;
	
	@NotNull
	@Schema(description = "A Revision PPRA Date. Should not have the same value or be less than his creation date")
	private LocalDateTime revisionDate;
	
	@NotNull
	@Schema(description = "A Status of PPRA (ACTIVE of INACTIVE)")
	private StatusType status;
	
	@DBRef(db = "user")
	@NotNull
	@Schema(description = "A association of a User document previously created")
	private User developerId;
	
	@DBRef(db = "user")
	@NotNull
	@Schema(description = "A association of a User document previously created")
	private User approverId;
	
	@NotNull
	@Schema(description = "Responsible Sector of this PPRA")
	private ResponsibleSectorType sector;
	
	@NotNull
	@NotBlank
	@Size(min = 50, max = 1000)
	@Schema(description = "A PPRS Purpose to exists")
	private String purpose;
	
	@NotNull
	@NotBlank
	@Size(min = 50, max = 1000)
	@Schema(description = "A PPRA Scope")
	private String scope;
	
	@NotNull
	@NotBlank
	@Size(min = 50, max = 5000)
	@Schema(description = "A PPRA Goals or Schedules")
	private String goal;
	
	@NotNull
	@Schema(description = "A Publish Date of PPRA. This field should have value equals or greater than his creation date")
	private LocalDateTime publishDate;
	
	@NotNull
	@NotEmpty
	@Schema(description = "A PPRA Promotion Departments. At least one section should be provided")
	private List<ResponsibleSectorType> promotionDepartments;
	
	@NotNull
	@Schema(description = "A PPRA Disclosure Method")
	private DisclosureMethodType disclosureMethod;
	
	@NotNull
	@Schema(description = "Should be greater than Publish Date")
	private LocalDateTime expirationDate;
	
	@Size(max = 500, min = 16)
	@Schema(description = "A Document id or url to index it to a PPRA")
	private String document;
	
	@Size(min = 20, max = 500)
	@Schema(description = "A Reson for a PPRA Revision")
	private String revisionReason;
	
	@Size(max = 500, min = 16)
	@Schema(description = "A PPRA responsible signature")
	private String signature;
	
	@Override
	public String getId() {
		return this.getCode();
	}

	@Override
	public void setId(String id) {
		this.setCode(id);
	}

}
