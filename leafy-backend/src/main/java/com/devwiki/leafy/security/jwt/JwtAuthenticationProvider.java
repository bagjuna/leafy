package com.devwiki.leafy.security.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.devwiki.leafy.global.common.exception.enums.BadStatusCode;
import com.devwiki.leafy.global.common.exception.type.UnAuthorizedException;
import com.devwiki.leafy.security.service.RestUserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final RestUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();

        log.info("Attempting to authenticate token: {}", token); // 2. 토큰이 제대로 들어왔는지 로그
        try {
            jwtUtil.validateToken(token); // 여기서 예외가 발생하면 catch로 넘어감

            log.info("Token validated successfully."); // 3. 토큰 검증 성공 로그

            String userId = jwtUtil.getUserIdFromToken(token);
            log.info("Extracted userId: {}", userId); // 4. 추출된 userId 로그
            UserDetails userDetails = userDetailsService.loadUserById(Long.parseLong(userId));

            log.info("User details loaded successfully for userId: {}", userId); // 5. 사용자 로딩 성공 로그
            return JwtAuthenticationToken.authenticated(
                    userDetails,
                    token,
                    userDetails.getAuthorities());
        } catch (Exception e) { // JwtException을 AuthenticationException 으로 변환
            throw new UnAuthorizedException(BadStatusCode.JWT_AUTHENTICATION_FAIL);
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
