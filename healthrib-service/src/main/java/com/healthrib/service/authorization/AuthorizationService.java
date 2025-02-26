package com.healthrib.service.authorization;

import static com.healthrib.enums.ValidationMessagesType.USER_NOT_FOUND;
import static com.healthrib.enums.user.permission.UserPermissionType.COWORKER;
import static com.healthrib.type.status.StatusType.ACTIVE;
import static java.time.LocalDateTime.now;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.healthrib.exceptions.UsernameNotFoundException;
import com.healthrib.model.user.User;
import com.healthrib.repository.user.UserRepository;
import com.healthrib.resources.Credentials;
import com.healthrib.resources.Token;
import com.healthrib.service.email.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorizationService {
	
	@Autowired
	private TokenProviderService provider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private EmailService emailService;
	
	public Token signIn(Credentials credentials) {
		String email = credentials.getEmail();
		User user = repository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(USER_NOT_FOUND);
		}
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(email, credentials.getPassword()));
		Token response = provider.createAccessToken(email, user.getRoles());
		repository.save(user);
		return response;
	}

	public Token refreshToken(String username, String token) {
		User user = repository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(USER_NOT_FOUND);
		}
		return provider.refreshToken(token);
	}

	public User signup(User user) throws Exception {
		String encoderName = "{pbkdf2}";
		user.setPassword(encoder.encode(user.getPassword()).substring(encoderName.length()));
		user.setCreatedAt(now());
		user.setStatus(ACTIVE);
		user.setVerified(false);
		user.setCreatedBy("client");
		if(isEmpty(user.getRoles())) {
			user.setRoles(Arrays.asList(COWORKER));
		}
		User saved = repository.save(user);
		log.info("POST | signup | User created successfully");
		emailService.send(user.getName(), user.getEmail());
		return saved;
	}

}
