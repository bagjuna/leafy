package com.devwiki.leafy.security.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import com.devwiki.leafy.dto.user.UserDto;
import com.devwiki.leafy.security.token.RestAuthenticationToken;
import com.devwiki.leafy.util.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final ObjectMapper objectMapper;

	public RestAuthenticationFilter(ObjectMapper objectMapper) {
		super(new AntPathRequestMatcher("/api/users/login", "POST"));
		this.objectMapper = objectMapper;
		// setSecurityContextRepository(getSecurityContextRepository(http));

	}

	public SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {
		SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
		if(securityContextRepository == null) {
			securityContextRepository = new DelegatingSecurityContextRepository(
				new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository()
			);
		}
		return  securityContextRepository;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException,
		IOException,
		ServletException {

		if (!HttpMethod.POST.name().equals(request.getMethod())) {
			throw new IllegalArgumentException("Authentication method not supported");
		}
		UserDto userDto = objectMapper.readValue(request.getReader(), UserDto.class);

		if (!StringUtils.hasText(userDto.getEmail()) || !StringUtils.hasText(userDto.getPassword())) {
			throw new AuthenticationServiceException("Username or Password not provided");
		}


		RestAuthenticationToken token = new RestAuthenticationToken(userDto.getEmail(), userDto.getPassword());

		return getAuthenticationManager().authenticate(token);
	}

}
