package com.healthrib.resources;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String username;
	private Boolean authenticated;
	private Date createdAt;
	private Date expiration;
	private String accessToken;
	private String refreshToken;

}
