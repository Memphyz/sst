package com.healthrib.exceptions;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date timestamp;
	private String message;
	private String description;

}