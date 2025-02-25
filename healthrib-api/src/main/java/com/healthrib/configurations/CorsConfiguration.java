package com.healthrib.configurations;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.healthrib.security.TokenFilter;
import com.healthrib.security.TokenProvider;

@Configuration
@EnableWebSecurity
public class CorsConfiguration {
	
	@Autowired
	private TokenProvider provider;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.httpBasic(AbstractHttpConfigurer::disable)
				.csrf(AbstractHttpConfigurer::disable)
				.addFilterBefore(new TokenFilter(provider), UsernamePasswordAuthenticationFilter.class)
				.cors(cors -> {})
				.authorizeHttpRequests(cors -> cors
						.requestMatchers("/authorization/**", "/swagger-ui.html**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.requestMatchers("/imup/**").permitAll()
						.requestMatchers("/api/v1/**").authenticated()
						.anyRequest().denyAll())
				.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
				.build();
	}

}