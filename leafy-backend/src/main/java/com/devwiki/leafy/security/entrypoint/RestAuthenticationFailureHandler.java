package com.devwiki.leafy.global.security.entrypoint;

import java.io.IOException;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.devwiki.leafy.Discord.service.DiscordService; // ⬅️ 1. import

@Slf4j
@RequiredArgsConstructor
@Component("restFailureHandler")
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private final ObjectMapper mapper;
	private final DiscordService discordService;
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, ServletException {
		log.info("REST Authentication Failure Handler called");
		// ⬇️ 5. 디스코드 알림 로직 추가 ⬇️
		try {
			String clientIp = getClientIp(request);
			String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURI());

			// "로그인 정보가 유효하지 않습니다" 메시지를 사용
			discordService.send4xxNotification(exception.getMessage(), requestInfo, clientIp);

		} catch (Exception e) {
			log.error("로그인 실패 Discord 알림 전송 중 오류 발생", e);
		}

		// --- (기존에 있던 JSON 에러 응답 로직) ---
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드
		response.setContentType("application/json;charset=UTF-8");

		// 예시: BadResponse 같은 공통 응답 DTO로 변환
		// Map<String, Object> errorDetails = Map.of(
		//     "status", 401,
		//     "message", exception.getMessage()
		// );
		// response.getWriter().write(objectMapper.writeValueAsString(errorDetails));

		// 간단한 텍스트 응답 (기존 로직에 맞게 수정 필요)
		response.getWriter().write("Error: " + exception.getMessage());
	}


	// IP 주소 가져오는 헬퍼 메서드
	private String getClientIp(HttpServletRequest request) {
		String clientIp = request.getHeader("X-Forwarded-For");
		if (clientIp != null && !clientIp.isBlank()) {
			return clientIp.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}
}
