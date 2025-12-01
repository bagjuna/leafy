package com.devwiki.leafy.security.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.devwiki.leafy.security.service.RestUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // 직접 주입
    private final RestUserDetailsService restUserDetailsService; // 직접 주입
    private final JwtAuthenticationConverter authenticationConverter; // 컨버터 주입

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        JwtAuthenticationToken authenticationRequest = authenticationConverter.convert(request);
        if (authenticationRequest == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 1. [변경] AuthenticationManager 우회. 직접 토큰 검증 및 Context 설정 시작
            String token = (String) authenticationRequest.getCredentials();
            jwtUtil.validateToken(token);

            String userId = jwtUtil.getUserIdFromToken(token);
            log.info("userId from token: {}", userId);
            // 2. UserDetails 로딩 (DB 조회)
            UserDetails userDetails = restUserDetailsService.loadUserById(Long.parseLong(userId));

            // 3. Context에 인증 객체 직접 설정
            Authentication authenticatedToken = JwtAuthenticationToken.authenticated(
                userDetails, token, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
            // log.info("FINAL SUCCESS: Security Context manually set for User: {}", userId); // ★★★ 최종 성공 로그 ★★★

        } catch (Exception e) {
            // 토큰 만료, 서명 오류 시 Context 설정하지 않고 다음 필터로 넘김 (401 유도)
            log.error("🔥🔥🔥 JWT 로직 치명적 오류 발생 🔥🔥🔥", e);
            SecurityContextHolder.clearContext(); // Context를 명시적으로 비움
            log.debug("JWT Token validation failed or user not found: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
