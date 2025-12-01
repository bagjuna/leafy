package com.devwiki.leafy.security.entrypoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.devwiki.leafy.dto.user.UserDto;
import com.devwiki.leafy.security.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("restSuccessHandler")
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final ObjectMapper mapper;
	private final JwtUtil jwtUtil; // 1. JwtUtil 주입
	private final RedisTemplate<String, String> redisTemplate; // 1. RedisTemplate 주입

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		// UserDto 또는 UserContext에서 userId를 가져와야 합니다.
		// (이 부분은 authentication.getPrincipal()의 실제 타입에 맞게 수정이 필요합니다)
		UserDto loginDto = (UserDto)authentication.getPrincipal();
		int userId = Math.toIntExact(loginDto.getUserId()); // 예시: userId를 가져옴
		// 2. JWT 토큰 생성
		String accessToken = jwtUtil.createAccessToken(userId);
		String refreshToken = jwtUtil.createRefreshToken(userId);

		// 3. ★ Redis에 Refresh Token 저장 (핵심) ★
		// Key: "RT:1", Value: "eyJ...", Duration: 30일 (JwtUtil 설정을 따름)
		redisTemplate.opsForValue().set(
			"RT:" + userId,
			refreshToken,
			jwtUtil.getRefreshTokenMaxAgeInSeconds(), // 예: 1209600 (14일)
			TimeUnit.SECONDS
		);

		// 4. 토큰을 JSON 응답으로 반환
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("accessToken", accessToken);
		responseData.put("refreshToken", refreshToken);
		responseData.put("user", loginDto);


		// 3. JSON 응답 전송
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8"); // 한글 깨짐 방지
		mapper.writeValue(response.getWriter(), responseData);

		clearAuthenticationAttributes(request);

	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
