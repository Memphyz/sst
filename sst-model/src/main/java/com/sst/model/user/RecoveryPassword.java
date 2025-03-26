package com.sst.model.user;

import static java.util.UUID.randomUUID;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
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
@Schema(description = "A model used to create a recovery password token and send it to email")
public class RecoveryPassword extends AbstractModel<String>{
	
	private static final long serialVersionUID = 1L;

	@NotNull
    @NotBlank
    @NotEmpty
    @Schema(description = "Token used to validate a user password reset")
    private String token;
    
    @CreatedDate
    @Schema(description = "A creation date to validate a token expiration after one month pending")
    private Date createdDate;
    
    @NotNull
    @Email
    @Schema(description = "A email user that requested a password reset")
    private String email;

    @DBRef
    @Schema(description = "User that requested a passoword reset")
    private User user;
    
    public RecoveryPassword(User user) {
        this.user = user;
        this.email = user.getEmail();
        createdDate = new Date();
        token = randomUUID().toString();
    }

}
