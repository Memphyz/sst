package bom.healthrib.service.authorization;

import static com.healthrib.enums.ValidationMessagesType.USER_NOT_FOUND;
import static com.healthrib.type.status.StatusType.ACTIVE;
import static java.time.LocalDateTime.now;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.healthrib.exceptions.UsernameNotFoundException;
import com.healthrib.model.user.User;
import com.healthrib.repository.user.UserRepository;
import com.healthrib.resources.Credentials;
import com.healthrib.resources.Token;
import com.healthrib.security.TokenProvider;


public class AuthorizationService {
	
	@Autowired
	private TokenProvider provider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserRepository repository;

	public Token signIn(Credentials credentials) {
		String username = credentials.getUsername();
		User user = repository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(USER_NOT_FOUND);
		}
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, credentials.getPassword()));
		Token response = provider.createAccessToken(username, user.getRoles());
		repository.save(user);
		return response;
	}

	public Token refreshToken(String username, String token) {
		User user = repository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(USER_NOT_FOUND);
		}
		return provider.refreshToken(token);
	}

	public User signup(User user) {
		String encoderName = "{pbkdf2}";
		user.setPassword(encoder.encode(user.getPassword()).substring(encoderName.length()));
		user.setCreatedAt(now());
		user.setStatus(ACTIVE);
		user.setVerified(false);
		user.setCreatedBy("client");
		return repository.save(user);
	}

}
