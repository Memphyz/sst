package com.healthrib.controller.authorization;

import static com.healthrib.enums.ValidationMessagesType.INVALID_CREDENTIALS;
import static com.healthrib.enums.ValidationMessagesType.INVALID_REFRESH_TOKEN;
import static com.healthrib.enums.ValidationMessagesType.REFRESH_TOKEN_NOT_PROVIDED;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.util.StringUtils.hasLength;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthrib.model.user.User;
import com.healthrib.resources.Credentials;
import com.healthrib.resources.Token;

import bom.healthrib.service.authorization.AuthorizationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("authorization")
public class AuthorizationController {
	
	@Autowired
	private AuthorizationService service;
	
	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody @NotNull @Valid Credentials credentials) {
		Token token = service.signIn(credentials);
		if(token == null) {
			return status(FORBIDDEN).body(INVALID_CREDENTIALS.getMessage());
		}
		return ok(token);
	}
	
	@PutMapping("/refresh/{username}")
	public ResponseEntity<?> refreshToken(@PathVariable @NotNull String username, @RequestHeader("Authorization") String bearer) {
		if(isNull(bearer) || !hasLength(bearer)) {
			return status(FORBIDDEN).body(REFRESH_TOKEN_NOT_PROVIDED.getMessage());
		}
		Token token = service.refreshToken(username, bearer);
		if(token == null) {
			return status(FORBIDDEN).body(INVALID_REFRESH_TOKEN.getMessage());
		}
		return ok(token);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<User> signUp(@RequestBody @NotNull @Valid User user) {
		return ok(service.signup(user));
	}

}
