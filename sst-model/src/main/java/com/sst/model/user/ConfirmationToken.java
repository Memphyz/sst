package com.sst.model.user;

import static java.util.UUID.randomUUID;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sst.abstracts.model.AbstractModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "A confirmation email token of a user created")
public class ConfirmationToken extends AbstractModel<String> {

		private static final long serialVersionUID = 1L;

		@NotNull
	    @NotBlank
	    @NotEmpty
	    @Schema(description = "A confirmation token to validate a email provided on user creation")
	    private String confirmationToken;
	    
	    @CreatedDate
	    @Indexed
	    @Schema(description = "A creation date of a user confirmation token")
	    private Date createdDate;
	    
	    @NotNull
	    @Email
	    @Schema(description = "A email of a user created")
	    private String email;

	    @DBRef
	    @Schema(description = "User created account")
	    private User user;
	    
	    public ConfirmationToken(User user) {
	        this.user = user;
	        this.email = user.getEmail();
	        createdDate = new Date();
	        confirmationToken = randomUUID().toString();
	    }
}
