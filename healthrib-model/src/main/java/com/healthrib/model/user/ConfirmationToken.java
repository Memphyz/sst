package com.healthrib.model.user;

import static java.util.UUID.randomUUID;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.healthrib.abstracts.model.AbstractModel;

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
public class ConfirmationToken extends AbstractModel<String> {

		private static final long serialVersionUID = 1L;

		@NotNull
	    @NotBlank
	    @NotEmpty
	    private String confirmationToken;
	    
	    @CreatedDate
	    private Date createdDate;

	    @DBRef
	    private User user;
	    
	    public ConfirmationToken(User user) {
	        this.user = user;
	        createdDate = new Date();
	        confirmationToken = randomUUID().toString();
	    }
}
