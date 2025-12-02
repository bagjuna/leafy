package com.devwiki.leafy.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import com.devwiki.leafy.Discord.service.DiscordService;
import com.devwiki.leafy.security.dsl.RestApiDsl;
import com.devwiki.leafy.security.entrypoint.RestAuthenticationEntryPoint;
import com.devwiki.leafy.security.entrypoint.RestAuthenticationFailureHandler;
import com.devwiki.leafy.security.entrypoint.RestAuthenticationSuccessHandler;
import com.devwiki.leafy.security.handler.RestAccessDeniedHandler;
import com.devwiki.leafy.security.jwt.JwtAuthenticationConverter;
import com.devwiki.leafy.security.jwt.JwtAuthenticationFilter;
import com.devwiki.leafy.security.jwt.JwtUtil;
import com.devwiki.leafy.security.service.RestUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthenticationProvider restAuthenticationProvider; // ID/PW 로그인용
	private final AuthenticationProvider jwtAuthenticationProvider;  // JWT 토큰 검증용

	private final RestAuthenticationSuccessHandler restSuccessHandler;
	private final RestAuthenticationFailureHandler restFailureHandler;
	private final ObjectMapper objectMapper;

	private final JwtUtil jwtUtil;
	private final RestUserDetailsService restUserDetailsService;
	private final JwtAuthenticationConverter jwtAuthenticationConverter;

	private final DiscordService discordService;
	@Bean
	public SecurityFilterChain restFilterChain(HttpSecurity http) throws Exception {

		AuthenticationManagerBuilder authenticationManagerBuilder =
			http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider); // 1. ID/PW 용
		authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider);  // 2. JWT 검증용
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();            // build() 는 최초 한번 만 호출해야 한다

		http

			.securityMatcher("/api/**")
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
				.requestMatchers("/api", "/api/users/login", "/api/users/signup","/api/users/reissue").permitAll()
				// .requestMatchers("/api/user").h9asAuthority("ROLE_USER")
				// .requestMatchers("/api/manager").hasAuthority("ROLE_MANAGER")
				// .requestMatchers("/api/admin").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated()
			)
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.addFilterBefore(
				new JwtAuthenticationFilter(jwtUtil, restUserDetailsService, jwtAuthenticationConverter),
				AuthorizationFilter.class
			)

			.authenticationManager(authenticationManager)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(new RestAuthenticationEntryPoint(objectMapper, discordService))
				.accessDeniedHandler(new RestAccessDeniedHandler(objectMapper))
			).with(new RestApiDsl<>(objectMapper), restDsl -> restDsl
				.restSuccessHandler(restSuccessHandler)
				.restFailureHandler(restFailureHandler)
				// .loginPage("/api/users/login")
				.loginProcessingUrl("/api/users/login")
			)

		;

		return http.build();
	}

}
