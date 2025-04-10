package com.sst.security;

import static com.sst.enums.ValidationMessagesType.EXPIRATED_TOKEN;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.GenericFilterBean;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sst.exceptions.ExceptionResponse;
import com.sst.service.authorization.TokenProviderService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenFilter extends GenericFilterBean {

	@Autowired
	private TokenProviderService provider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = provider.resolveToken((HttpServletRequest) request);
		try {
			if (token != null && provider.validateToken(token)) {
				Authentication auth = provider.getAuth(token);
				if (auth != null) {
					getContext().setAuthentication(auth);
				}
			}
		} catch (TokenExpiredException ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(FORBIDDEN.value());
			httpResponse.setContentType("application/json");
			httpResponse.setCharacterEncoding("UTF-8");
			ExceptionResponse body = new ExceptionResponse(new Date(),
					ex.getMessage(),
					ex.getLocalizedMessage(),
					EXPIRATED_TOKEN.getMessage());
			ObjectMapper mapper = new ObjectMapper();
		    mapper.writeValue(response.getWriter(), body);
		}
		chain.doFilter(request, response);

	}
}
