package com.sst.configurations;

import static org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import com.sst.service.authorization.TokenProviderService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {
	
	private static final int DEFAULT_SALT_LENGTH = 16;

	private static final int DEFAULT_ITERATIONS = 310000;

	@Autowired
	private TokenProviderService provider;

	@Bean
	PasswordEncoder encoder() {
		Map<String, PasswordEncoder> encoders = new HashMap<String, PasswordEncoder>();
		Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(provider.getSecret(), DEFAULT_SALT_LENGTH, DEFAULT_ITERATIONS, PBKDF2WithHmacSHA256);
		encoders.put("pbkdf2", encoder);
		DelegatingPasswordEncoder passEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		passEncoder.setDefaultPasswordEncoderForMatches(encoder);
		return passEncoder;
	}
	
	@Bean
	AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	

}
