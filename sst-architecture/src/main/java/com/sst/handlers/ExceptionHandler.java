package com.sst.handlers;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.sst.exceptions.AuthenticationException;
import com.sst.exceptions.ExceptionResponse;
import com.sst.exceptions.PasswordMatchingException;
import com.sst.exceptions.TokenException;
import com.sst.exceptions.UserNotFound;
import com.sst.exceptions.UsernameNotFoundException;

@RestControllerAdvice
public class ExceptionHandler {
	
	@org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
	public final ResponseEntity<ExceptionResponse> handleException(AuthenticationException ex, WebRequest request) {
			ExceptionResponse response = getException(ex.getMessage(), request);
			return new ResponseEntity<ExceptionResponse>(response, FORBIDDEN);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleException(UsernameNotFoundException ex, WebRequest request) {
			ExceptionResponse response = getException(ex.getMessage(), request);
			return new ResponseEntity<ExceptionResponse>(response, NOT_FOUND);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(UserNotFound.class)
	public final ResponseEntity<ExceptionResponse> handleException(UserNotFound ex, WebRequest request) {
			ExceptionResponse response = getException(ex.getMessage(), request);
			return new ResponseEntity<ExceptionResponse>(response, NOT_FOUND);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(PasswordMatchingException.class)
	public final ResponseEntity<ExceptionResponse> handleException(PasswordMatchingException ex, WebRequest request) {
			ExceptionResponse response = getException(ex.getMessage(), request);
			return new ResponseEntity<ExceptionResponse>(response, NOT_FOUND);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(TokenException.class)
	public final ResponseEntity<ExceptionResponse> handleException(TokenException ex, WebRequest request) {
			ExceptionResponse response = getException(ex.getMessage(), request);
			return new ResponseEntity<ExceptionResponse>(response, FORBIDDEN);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleException(Exception ex, WebRequest request) {
			ExceptionResponse response = getException(ex.getMessage(), request);
			return new ResponseEntity<ExceptionResponse>(response, INTERNAL_SERVER_ERROR);
	}
	
	public final ExceptionResponse getException(String message, WebRequest request) {
		return new ExceptionResponse(
				new Date(),
				message,
				request.getDescription(false));
	}
}
