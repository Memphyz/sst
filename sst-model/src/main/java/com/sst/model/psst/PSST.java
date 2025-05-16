package com.sst.model.psst;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.sst.abstracts.model.auditable.AbstractModelAuditable;
import com.sst.annotations.Future;
import com.sst.model.sector.Sector;
import com.sst.model.user.User;
import com.sst.enums.DisclosureMethodType;
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
@Document(collection = "psst")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PSST model")
@EqualsAndHashCode(callSuper = true)
public class PSST extends AbstractModelAuditable<String>{
	
	private static final long serialVersionUID = 1L;

	@NotNull
	@MongoId
	@NotBlank
	@Indexed(unique = true)
	@Size(min = 10, max = 20)
	@Schema(description = "A unique Code of PSST")
	private String code;
	
	@NotNull
	@NotBlank
	@Size(min = 10, max = 120)
	private String name;
	
	
	@NotNull
	@NotBlank
	@Size(min = 3, max = 60)
	@Pattern(regexp = "^\\d+\\.\\d+$")
	private String version;
	
	@NotNull
	@NotBlank
	@Size(min = 3, max = 60)
	@Pattern(regexp = "^\\d+\\.\\d+$")
	private String previeusVersion;
	
	@NotNull
	@Future
	private LocalDate revisionDate;
	
	@NotNull
	private StatusType status;
	
	@NotEmpty
	private List<String> ccMails;
	
	@DBRef(db = "user")
	@NotNull
	private User elaborator;
	
	@DBRef(db = "user")
	@NotNull
	private User approver;
	
	@NotNull
	@DBRef(db = "sector")
	private Sector responsibleSector;
	
	@NotNull
	@NotBlank
	@Size(min = 10, max = 1000)
	private String objective;
	
	@NotNull
	@NotBlank
	@Size(min = 10, max = 5000)
	private String guidelines;
	
	@NotBlank
	@Size(min = 10, max = 1000)
	private String normativeRefs;
	
	@Size(min = 16, max = 500)
	private String document;
	
	@NotBlank
	@Size(min = 5, max = 500)
	private String reasonRevision;
	
	@NotNull
	@Future
	private LocalDate publishDate;
		
	@DBRef(db = "sector")
	@NotNull
	@NotEmpty
	private List<Sector> disseminationSectors;
	
	@NotNull
	private DisclosureMethodType disclosureMethod;
	
	@NotNull
	private LocalDate politicalExpirationDate;

	@Override
	public String getId() {
		return this.code;
	}

	@Override
	public void setId(String id) {
		this.code = id;
	}
}
