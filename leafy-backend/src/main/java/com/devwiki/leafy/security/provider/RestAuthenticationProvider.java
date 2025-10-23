package com.devwiki.leafy.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.devwiki.leafy.dto.user.UserContext;
import com.devwiki.leafy.security.token.RestAuthenticationToken;

import lombok.RequiredArgsConstructor;

@Component("restAuthenticationProvider")
@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
 		String loginId = authentication.getName();
		String password = (String)authentication.getCredentials();

		UserContext userContext = (UserContext)userDetailsService.loadUserByUsername(loginId);

		if (!passwordEncoder.matches(password, userContext.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}


		return new RestAuthenticationToken(userContext.getAuthorities(), userContext.getLoginDto(), null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(RestAuthenticationToken.class);
	}

}
