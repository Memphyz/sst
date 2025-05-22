package com.sst.model.pcmso;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.sst.abstracts.model.auditable.AbstractModelAuditable;
import com.sst.annotations.Future;
import com.sst.enums.DisclosureMethodType;
import com.sst.enums.StatusType;
import com.sst.model.sector.Sector;
import com.sst.model.user.User;

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
@Document(collection = "pcmso")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "PCMSO model")
@EqualsAndHashCode(callSuper = true)
public class PCMSO extends AbstractModelAuditable<String> {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@MongoId
	@NotBlank
	@Indexed(unique = true)
	@Size(min = 10, max = 20)
	@Schema(description = "A unique Code of PCMSO")
	public String code;
	
	@NotNull
	@NotBlank
	@Size(min = 5, max = 120)
	public String name;
	
	@NotNull
	@NotBlank
	@Size(min = 3, max = 60)
	@Pattern(regexp = "^\\d+\\.\\d+$")
	private String version;
	
	@Future
	public LocalDate revisionDate;
	
	@NotNull
	@Future
	private LocalDate publishDate;
	
	@NotNull
	public StatusType status;
	
	@NotNull
	@DBRef(db = "user")
	public User elaborator;
	
	@NotNull
	@DBRef(db = "user")
	public User approver;
	
	@NotNull
	@DBRef(db = "sector")
	public Sector sector;
	
	@NotNull
	@NotBlank
	@Size(min = 5, max = 1000)
	public String objective;
	
	@Size(min = 5, max = 500)
	private String reasonRevision;
	
	@NotNull
	@NotBlank
	@Size(min = 5, max = 1000)
	public String scope;
	
	@NotNull
	@NotBlank
	@Size(min = 5, max = 5000)
	public String goals;
	
	@NotBlank
	@Size(min = 10, max = 1000)
	private String normativeRefs;
	
	@Size(min = 16, max = 500)
	private String document;
	
	@NotBlank
	@Size(min = 3, max = 60)
	@Pattern(regexp = "^\\d+\\.\\d+$")
	private String previeusVersion;
	
	@DBRef(db = "sector")
	@NotNull
	@NotEmpty
	private List<Sector> disseminationSectors;
	
	@NotNull
	private DisclosureMethodType disclosureMethod;
	
	@NotNull
	private LocalDate expirationDate;
	
	@Override
	public String getId() {
		return this.code;
	}

	@Override
	public void setId(String id) {
		this.code = id;
	}
	
	

}
