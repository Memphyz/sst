package com.sst.configurations;

import static java.util.Optional.ofNullable;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import com.sst.model.user.User;


@Configuration
@EnableMongoAuditing
public class SpringSecurityAuditorAware implements AuditorAware<String> {
	
	@Override
	public Optional<String> getCurrentAuditor() {
		return ofNullable(getContext())
	            .map(SecurityContext::getAuthentication)
	            .filter(Authentication::isAuthenticated)
	            .map(Authentication::getPrincipal)
	            .map(principal -> principal instanceof User user ? user.getEmail() : principal.toString());
	}

}
