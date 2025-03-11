package com.sst.security;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.GenericFilterBean;

import com.sst.service.authorization.TokenProviderService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenFilter extends GenericFilterBean  {
	
	@Autowired
	private TokenProviderService provider;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = provider.resolveToken((HttpServletRequest) request);
		if(token != null && provider.validateToken(token)) {
			Authentication auth = provider.getAuth(token);
			if(auth != null) {
				getContext().setAuthentication(auth);
			}
		}
		chain.doFilter(request, response);
		
	}
}
