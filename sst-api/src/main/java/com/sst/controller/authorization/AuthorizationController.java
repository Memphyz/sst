package com.sst.controller.authorization;

import static com.sst.enums.ValidationMessagesType.INVALID_CREDENTIALS;
import static com.sst.enums.ValidationMessagesType.INVALID_REFRESH_TOKEN;
import static com.sst.enums.ValidationMessagesType.REFRESH_TOKEN_NOT_PROVIDED;
import static com.sst.enums.ValidationMessagesType.USER_ALREADY_EXISTS;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
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

import com.sst.exceptions.TokenException;
import com.sst.exceptions.UserAlreadyExistsException;
import com.sst.model.user.User;
import com.sst.resources.Credentials;
import com.sst.resources.Login;
import com.sst.resources.ResetPassword;
import com.sst.resources.Token;
import com.sst.service.authorization.AuthorizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(description = "login, signup, refresh token, email confirmation, etc...", name = "Authorization")
@RequestMapping("authorization")
public class AuthorizationController {

	@Autowired
	private AuthorizationService service;

	@Operation(description = "Used to sign in a user by email and password as credentials.")
	@PostMapping("/signin")
	public ResponseEntity<?> signIn(
			@RequestBody @NotNull @Valid @Parameter(description = "Login credentials used to sign in by email and password") Login login) {
		log.info("POST | signIn | Sign in with credentials: {}", login.toString());
		Token token = service.signIn(login);
		if (token == null) {
			throw new TokenException(INVALID_CREDENTIALS);
		}
		return ok(token);
	}

	@Operation(description = "Used to update a access token by refresh token as Authorization in header and username as path variable.")
	@PutMapping("/refresh/{username}")
	public ResponseEntity<?> refreshToken(
			@PathVariable @NotNull @Parameter(description = "User username to locate it on database") String username,
			@Parameter(description = "A refresh token to update a access token") @RequestHeader("Authorization") String bearer) {
		log.info("PUT | refreshToken | Refreshing token from user: {}", username);
		if (isNull(bearer) || !hasLength(bearer)) {
			throw new TokenException(REFRESH_TOKEN_NOT_PROVIDED);
		}
		Token token = service.refreshToken(username, bearer);
		if (token == null) {
			throw new TokenException(INVALID_REFRESH_TOKEN);
		}
		return ok(token);
	}

	@Operation(description = "Used to create a new user. After user crestion a confirmation email will be sent to a email provided.")
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(
			@RequestBody @Valid @Parameter(description = "A user data to be created on database") Credentials credentials) {
		log.info("POST | signUp | Sign 	 with credentials: {}", credentials.toString());
		if (service.hasUserByEmail(credentials.getEmail())) {
			throw new UserAlreadyExistsException(USER_ALREADY_EXISTS);
		}
		log.info("POST | signUp | Signing up user: {}", credentials.getName());
		return new ResponseEntity<User>(service.signup(credentials), CREATED);
	}

	@Operation(description = "Used to confirm a email provided by user on his creation, for param we recive a email provided and a token that user recived on email link.")
	@PostMapping("/verify/{email}/{confirmationToken}")
	public ResponseEntity<?> confirmEmail(
			@PathVariable @Parameter(description = "A email associated to a user confirmation token") String email,
			@PathVariable @Parameter(description = "A confirmation token used to confirm email") String confirmationToken) {
		return service.confirmEmail(email, confirmationToken);
	}

	@Operation(description = "Used to send a recovery token to reset user password")
	@PostMapping("/recovery/{email}")
	public ResponseEntity<?> recoverPassword(
			@PathVariable @Parameter(description = "A email associated to a user password recovery") String email) {
		service.sendRecoverPasswordToken(email);
		return ok().build();
	}
	
	@Operation(description = "Used to reset user password")
	@PostMapping("/reset")
	public ResponseEntity<?> resetPassword(
			@RequestBody @Valid @Parameter(description = "A body data required to reset user password by email") ResetPassword body) {
		service.resetPassword(body);
		return ok().build();
	}

}
