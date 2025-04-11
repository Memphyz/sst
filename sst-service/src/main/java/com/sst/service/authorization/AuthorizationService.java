package com.sst.service.authorization;

import static com.sst.enums.ValidationMessagesType.EXPIRATED_TIME;
import static com.sst.enums.ValidationMessagesType.INVALID_TOKEN;
import static com.sst.enums.ValidationMessagesType.TOKEN_NOT_FOUND;
import static com.sst.enums.ValidationMessagesType.USER_NOT_FOUND;
import static com.sst.enums.ValidationMessagesType.USER_VERIFIED;
import static com.sst.enums.user.permission.UserPermissionType.COWORKER;
import static com.sst.type.status.StatusType.ACTIVE;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.time.DateUtils.addMonths;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sst.abstracts.service.AbstractService;
import com.sst.exceptions.PasswordMatchingException;
import com.sst.exceptions.TokenException;
import com.sst.exceptions.UserNotFound;
import com.sst.exceptions.UsernameNotFoundException;
import com.sst.model.user.ConfirmationToken;
import com.sst.model.user.RecoveryPassword;
import com.sst.model.user.User;
import com.sst.repository.user.ConfirmationTokenRepository;
import com.sst.repository.user.RecoveryPasswordRepository;
import com.sst.repository.user.UserRepository;
import com.sst.resources.Credentials;
import com.sst.resources.Login;
import com.sst.resources.ResetPassword;
import com.sst.resources.Token;
import com.sst.service.email.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorizationService extends AbstractService<User, UserRepository> {

	@Autowired
	private TokenProviderService provider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository repository;

	@Autowired
	private ConfirmationTokenRepository confirmationRepository;
	
	@Autowired
	private RecoveryPasswordRepository recoveryRepository;

	@Autowired
	private EmailService emailService;
	
	private static final String encoderName = "{pbkdf2}";

	public ResponseEntity<?> confirmEmail(String email, String token) {
		User user = repository.findByEmail(email);
		if (user == null) {
			return ResponseEntity.status(NOT_FOUND).body(USER_NOT_FOUND);
		}
		ConfirmationToken confirmation = confirmationRepository.findByConfirmationToken(token);

		if (confirmation == null) {
			return ResponseEntity.status(NOT_FOUND).body(TOKEN_NOT_FOUND);
		}
		user.setVerified(true);
		repository.save(user);
		log.info("confirmEmail | User email {} verified successfully", confirmation.getUser().getEmail());
		confirmationRepository.deleteByConfirmationToken(token);
		return ResponseEntity.status(OK).body(USER_VERIFIED);
	}

	public Token signIn(Login login) {
		String email = login.getEmail();
		User user = repository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(USER_NOT_FOUND);
		}
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, login.getPassword()));
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

	@Transactional
	public User signup(Credentials credential) {
		User user = new User();
		user.setName(credential.getName());
		user.setEmail(credential.getEmail());
		user.setRoles(credential.getRoles());
		user.setPassword(encoder.encode(credential.getPassword()).substring(encoderName.length()));
		user.setCreatedAt(now());
		user.setStatus(ACTIVE);
		user.setVerified(false);
		user.setCreatedBy("client");
		if (isEmpty(user.getRoles())) {
			user.setRoles(Arrays.asList(COWORKER));
		}
		if(confirmationRepository.existsByEmail(user.getEmail())) {
			confirmationRepository.deleteByEmail(user.getEmail());
		}
		ConfirmationToken confirmation = new ConfirmationToken(user);
		confirmationRepository.save(confirmation);
		User saved = repository.save(user);
		log.info("POST | signup | User created successfully");
		new Thread(() -> {
			try {
				emailService.send(user.getName(), user.getEmail(), "email.html", "/authorization/verify/" + user.getEmail() + "/" + confirmation.getConfirmationToken());
				log.info("POST | signup | E-mail confirmation was sended successfully!");
			} catch (Exception e) {
				log.error("An error occurred on email send:");
				e.printStackTrace();
			}
		}).start();
		return saved;
	}
	
	public ConfirmationToken findConfirmationTokenByEmail(String email) {
		return confirmationRepository.findByEmail(email);
	}
	
	public RecoveryPassword sendRecoverPasswordToken(String email) {
		User user = repository.findByEmail(email);
		if(user == null) {
			throw new UserNotFound();
		}
		if(recoveryRepository.existsByEmail(user.getEmail())) {
			recoveryRepository.deleteByEmail(user.getEmail());
		}
		RecoveryPassword recovery = new RecoveryPassword(user);
		recoveryRepository.save(recovery);
		new Thread(() -> {
			try {
				emailService.send(user.getName(), user.getEmail(), "recovery.html", "/authorization/recovery/" + user.getEmail() + "/" + recovery.getToken());
				log.info("POST | signup | E-mail recovery was sended successfully!");
			} catch (Exception e) {
				log.error("An error occurred on email send:");
				e.printStackTrace();
			}
		}).start();
		return recovery;
	}

	public boolean hasUserByEmail(String email) {
		return repository.existsByEmail(email);
	}
	
	public void resetPassword(ResetPassword body, String token) {
		User user = repository.findByEmail(body.getEmail());
		if(user == null) {
			throw new UserNotFound();
		}
		RecoveryPassword recovery = recoveryRepository.findByEmail(body.getEmail());
		if(!StringUtils.equals(recovery.getToken(), token)) {
			throw new TokenException(INVALID_TOKEN);
		}
		Date expiration = new Date();
		addMonths(expiration, 1);
		if(recovery.getCreatedDate().after(expiration)) {
			throw new TokenException(EXPIRATED_TIME);
		}
		if(body.getPassword().equals(body.getConfirmPassword())) {
			user.setPassword(encoder.encode(body.getPassword()).substring(encoderName.length()));
			repository.save(user);
			recoveryRepository.delete(recovery);
			return;
		}
		throw new PasswordMatchingException();
	}

}
