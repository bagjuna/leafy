package com.devwiki.leafy.security.jwt;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.devwiki.leafy.security.service.RestUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
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
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // "/api/users/reissue" 경로는 필터 검사를 건너뜁니다.
        return path.equals("/api/users/reissue");
    }

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
            // 2. UserDetails 로딩 (DB 조회)
            UserDetails userDetails = restUserDetailsService.loadUserById(Long.parseLong(userId));

            // 3. Context에 인증 객체 직접 설정
            Authentication authenticatedToken = JwtAuthenticationToken.authenticated(
                userDetails, token, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
            // log.info("FINAL SUCCESS: Security Context manually set for User: {}", userId); // ★★★ 최종 성공 로그 ★★★

        } catch (SignatureException | ExpiredJwtException e) {
            // [변경] 서명 오류나 만료는 해킹 시도가 아니라면 흔한 일이므로 error 대신 info나 warn으로 찍습니다.
            log.warn("JWT 유효성 검증 실패 (정상적인 만료 혹은 서명 불일치): {}", e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            // 그 외의 진짜 알 수 없는 에러만 Error로 찍습니다.
            log.error("🔥🔥🔥 JWT 처리 중 알 수 없는 오류 발생 🔥🔥🔥", e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
