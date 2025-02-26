package com.healthrib.service.authorization;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static com.healthrib.enums.ValidationMessagesType.FORBIDDEN;
import static java.util.Base64.getEncoder;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.healthrib.enums.user.permission.UserPermissionType;
import com.healthrib.exceptions.AuthenticationException;
import com.healthrib.resources.Token;

import com.healthrib.service.user.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;


@Service
public class TokenProviderService {
	
	@Getter
	@Value("${security.jwt.token.secret-key:default-key}")
	private String secret;
	
	@Value("${security.jwt.token.expire-length:3600000}")
	private Long expire;
	
	@Autowired
	private UserService service;
	
	private static final String BEARER_NAME = "Bearer ";
	private static final String AUTH = "Authorization";
	
	Algorithm algorithm = null;
	
	@PostConstruct
	protected void init() {
		secret = getEncoder().encodeToString(secret.getBytes());
		algorithm = HMAC256(secret.getBytes());
	}
	
	public Token createAccessToken(String email, List<UserPermissionType> roles) {
		Date date = new Date();
		Date validity = new Date(date.getTime() + expire);
		String accessToken = getAccessToken(email, roles, date, validity);
		String refreshToken = getRefreshToken(email, roles, date);
		return new Token(email, date, validity, accessToken, refreshToken);
	}
	
	
	public Token refreshToken(String refreshToken) {
		refreshToken = getToken(refreshToken);
		DecodedJWT decoded = decodedToken(refreshToken);
		return createAccessToken(decoded.getSubject(), decoded.getClaim("roles").asList(UserPermissionType.class));
	}

	private String getRefreshToken(String email, List<UserPermissionType> roles, Date date) {
		Date validity = new Date(date.getTime() + (expire * 3));
		return JWT.create()
				.withClaim("roles", roles.stream().map(role -> role.getName()).toList())
				.withIssuedAt(date)
				.withExpiresAt(validity)
				.withSubject(email)
				.sign(algorithm)
				.strip();
	}

	private String getAccessToken(String email, List<UserPermissionType> roles, Date date, Date validity) {
		String issueUrl = fromCurrentContextPath().build().toUriString();
		return JWT.create()
				.withClaim("roles", roles.stream().map(role -> role.getName()).toList())
				.withIssuedAt(date)
				.withExpiresAt(validity)
				.withSubject(email)
				.withIssuer(issueUrl)
				.sign(algorithm)
				.strip();
	}
	
	public Authentication getAuth(String token) {
		DecodedJWT decoded = decodedToken(token);
		UserDetails user = service.loadUserByUsername(decoded.getSubject());
		return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
	}

	private DecodedJWT decodedToken(String token) {
		Algorithm alg = HMAC256(secret.getBytes());
		return JWT.require(alg).build().verify(token);
	}
	
	public String resolveToken(HttpServletRequest request) {
		String auth = request.getHeader(AUTH);
		String token = getToken(auth);
		return token;
	}
	
	public String getToken(String token) {
		return token != null && token.startsWith(BEARER_NAME) ? token.substring(BEARER_NAME.length()) : token;
	}
	
	public boolean validateToken(String token) {
		DecodedJWT decoded = decodedToken(token);
		try {
			if(decoded.getExpiresAt().before(new Date())) {
				return false;
			}
		} catch (Exception e) {
			throw new AuthenticationException(FORBIDDEN);
		}
		return true;
	}
}
