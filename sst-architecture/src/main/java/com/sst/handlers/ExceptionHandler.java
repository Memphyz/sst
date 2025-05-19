package com.sst.handlers;

import static com.sst.enums.ValidationMessagesType.ALREADY_EXISTS;
import static com.sst.enums.ValidationMessagesType.EXPIRATED_TOKEN;
import static com.sst.enums.ValidationMessagesType.INVALID_CREDENTIALS;
import static com.sst.enums.ValidationMessagesType.INVALID_TOKEN;
import static com.sst.enums.ValidationMessagesType.PASSWORD_CONFLICT;
import static com.sst.enums.ValidationMessagesType.RESOURCE_NOT_FOUND;
import static com.sst.enums.ValidationMessagesType.RESOURCE_VALIDATION;
import static com.sst.enums.ValidationMessagesType.UNKNOWN;
import static com.sst.enums.ValidationMessagesType.USER_NOT_FOUND;
import static com.sst.enums.ValidationMessagesType.USER_ROLE_PERMISSION;
import static com.sst.utils.StringUtils.toSkakeCase;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import java.util.Arrays;
import java.util.Date;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.sst.enums.ValidationMessagesType;
import com.sst.exceptions.AuthenticationException;
import com.sst.exceptions.ExceptionResponse;
import com.sst.exceptions.PasswordMatchingException;
import com.sst.exceptions.ResourceNotFound;
import com.sst.exceptions.ResourceValidationException;
import com.sst.exceptions.TokenException;
import com.sst.exceptions.UserNotFound;
import com.sst.exceptions.UserPermissionDenied;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
	public final ResponseEntity<ExceptionResponse> handleException(AuthenticationException ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, INVALID_CREDENTIALS);
		return new ResponseEntity<ExceptionResponse>(response, FORBIDDEN);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleException(UsernameNotFoundException ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, USER_NOT_FOUND);
		return new ResponseEntity<ExceptionResponse>(response, NOT_FOUND);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(com.sst.exceptions.UsernameNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleException(com.sst.exceptions.UsernameNotFoundException ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, USER_NOT_FOUND);
		return new ResponseEntity<ExceptionResponse>(response, NOT_FOUND);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(UserNotFound.class)
	public final ResponseEntity<ExceptionResponse> handleException(UserNotFound ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, USER_NOT_FOUND);
		return new ResponseEntity<ExceptionResponse>(response, NOT_FOUND);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(PasswordMatchingException.class)
	public final ResponseEntity<ExceptionResponse> handleException(PasswordMatchingException ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, PASSWORD_CONFLICT);
		return new ResponseEntity<ExceptionResponse>(response, NOT_FOUND);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(TokenException.class)
	public final ResponseEntity<ExceptionResponse> handleException(TokenException ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, INVALID_TOKEN);
		return new ResponseEntity<ExceptionResponse>(response, FORBIDDEN);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFound.class)
	public final ResponseEntity<ExceptionResponse> handleException(ResourceNotFound ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, RESOURCE_NOT_FOUND);
		return new ResponseEntity<ExceptionResponse>(response, NOT_FOUND);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
	public final ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException ex,
			WebRequest request) {
		String message = toSkakeCase(ex.getFieldError().getCode()).toUpperCase() + ": " + ex.getFieldError().getField();
		String detail = String.join(", ",
				ex.getFieldErrors().stream()
				.map(field -> toSkakeCase(field.getCode()).toUpperCase() + ": "
						+ field.getField() + "[" +
						String.join(", ", Arrays.asList(field.getArguments()).stream()
								.filter(argument -> !(argument instanceof DefaultMessageSourceResolvable))
								.map(argument -> argument.toString()).toList())
						+ "]").toList());
		ExceptionResponse response = getException(message, detail, RESOURCE_VALIDATION);
		return new ResponseEntity<ExceptionResponse>(response, INTERNAL_SERVER_ERROR);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(DuplicateKeyException.class)
	public final ResponseEntity<ExceptionResponse> handleException(DuplicateKeyException ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, ALREADY_EXISTS);
		return new ResponseEntity<ExceptionResponse>(response, CONFLICT);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(TokenExpiredException.class)
	public final ResponseEntity<ExceptionResponse> handleException(TokenExpiredException ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, EXPIRATED_TOKEN);
		return new ResponseEntity<ExceptionResponse>(response, FORBIDDEN);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(UserPermissionDenied.class)
	public final ResponseEntity<ExceptionResponse> handleException(UserPermissionDenied ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, USER_ROLE_PERMISSION);
		return new ResponseEntity<ExceptionResponse>(response, FORBIDDEN);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(ResourceValidationException.class)
	public final ResponseEntity<ExceptionResponse> handleException(ResourceValidationException ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, RESOURCE_VALIDATION);
		return new ResponseEntity<ExceptionResponse>(response, UNPROCESSABLE_ENTITY);
	}
	
	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleException(Exception ex, WebRequest request) {
		ExceptionResponse response = getException(ex, request, UNKNOWN);
		return new ResponseEntity<ExceptionResponse>(response, INTERNAL_SERVER_ERROR);
	}

	public final ExceptionResponse getException(Exception ex, WebRequest request, ValidationMessagesType type) {
		ex.printStackTrace();
		log.error("ExceptionHandler | request | {} | {}", ex.getMessage(), type.getMessage());
		return new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false), type.getMessage());
	}

	public final ExceptionResponse getException(Exception ex,String detail, ValidationMessagesType type) {
		ex.printStackTrace();
		log.error("ExceptionHandler | ex | {} | {} | {}", ex.getMessage(), detail, type.getMessage());
		return new ExceptionResponse(new Date(), ex.getMessage(), detail, type.getMessage());
	}
	
	public final ExceptionResponse getException(String message,String detail, ValidationMessagesType type) {
		log.error("ExceptionHandler | message | {} | {} | {}", message, detail, type.getMessage());
		return new ExceptionResponse(new Date(), message, detail, type.getMessage());
	}
}
