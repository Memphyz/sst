package com.sst.handlers;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.sst.exceptions.AuthenticationException;
import com.sst.exceptions.ExceptionResponse;
import com.sst.exceptions.UsernameNotFoundException;

@RestControllerAdvice
public class ExceptionHandler {
	
	@org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
	public final ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
			ExceptionResponse response = getException(ex.getMessage(), request);
			return new ResponseEntity<ExceptionResponse>(response, FORBIDDEN);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
			ExceptionResponse response = getException(ex.getMessage(), request);
			return new ResponseEntity<ExceptionResponse>(response, NOT_FOUND);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleUnknownException(Exception ex, WebRequest request) {
			ExceptionResponse response = getException(ex.getMessage(), request);
			return new ResponseEntity<ExceptionResponse>(response, FORBIDDEN);
	}
	
	public final ExceptionResponse getException(String message, WebRequest request) {
		return new ExceptionResponse(
				new Date(),
				message,
				request.getDescription(false));
	}
}
