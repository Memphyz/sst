package com.sst.configurations;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sst.model.user.User;

public class SpringSecurityAuditorAware implements AuditorAware<User> {

	@Override
	public Optional<User> getCurrentAuditor() {
		return Optional.ofNullable(SecurityContextHolder.getContext())
	            .map(SecurityContext::getAuthentication)
	            .filter(Authentication::isAuthenticated)
	            .map(Authentication::getPrincipal)
	            .map(User.class::cast);
	}

}
