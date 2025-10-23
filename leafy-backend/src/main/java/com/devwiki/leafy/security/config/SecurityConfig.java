package com.devwiki.leafy.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.devwiki.leafy.security.dsl.RestApiDsl;
import com.devwiki.leafy.security.entrypoint.RestAuthenticationEntryPoint;
import com.devwiki.leafy.security.entrypoint.RestAuthenticationFailureHandler;
import com.devwiki.leafy.security.entrypoint.RestAuthenticationSuccessHandler;
import com.devwiki.leafy.security.handler.RestAccessDeniedHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final AuthenticationProvider restAuthenticationProvider;
	private final RestAuthenticationSuccessHandler restSuccessHandler;
	private final RestAuthenticationFailureHandler restFailureHandler;
	private final ObjectMapper objectMapper;
	@Bean
	public SecurityFilterChain restFilterChain(HttpSecurity http) throws Exception {

		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
			AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();            // build() 는 최초 한번 만 호출해야 한다

		http
			.securityMatcher("/api/**")
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
				.requestMatchers("/api", "/api/users/login").permitAll()
				.requestMatchers("/api/user").hasAuthority("ROLE_USER")
				.requestMatchers("/api/manager").hasAuthority("ROLE_MANAGER")
				.requestMatchers("/api/admin").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated()
			)
			.csrf(AbstractHttpConfigurer::disable)
			// .addFilterBefore(restAuthenticationFilter(http, authenticationManager), UsernamePasswordAuthenticationFilter.class)
			.authenticationManager(authenticationManager)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(new RestAuthenticationEntryPoint(objectMapper))
				.accessDeniedHandler(new RestAccessDeniedHandler(objectMapper))
			).with(new RestApiDsl<>(objectMapper), restDsl -> restDsl
				.restSuccessHandler(restSuccessHandler)
				.restFailureHandler(restFailureHandler)
				.loginPage("/api/users/login")  // 이거 없어도 동작함
				.loginProcessingUrl("/api/users/login")  // 이거 없어도 동작함
			)

		;

		return http.build();
	}

}
