package com.devwiki.leafy.security.entrypoint;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.devwiki.leafy.Discord.service.DiscordService;
import com.devwiki.leafy.global.common.exception.type.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    private final DiscordService discordService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        log.warn("AuthenticationEntryPoint triggered: {}", authException.getMessage());
        String clientIp = getClientIp(request);
        String requestInfo = String.format("%s %s", request.getMethod(), request.getRequestURI());

        // authException.getCause()에 원본 예외(CustomException)가 들어있습니다.
        Throwable cause = authException.getCause();
        if (cause instanceof CustomException) {
            CustomException customException = (CustomException)cause;
            // 401 예외이므로 4xx 알림을 보냅니다.
            discordService.send4xxNotification(
                customException.getMessage(),
                requestInfo,
                clientIp
            );

            // (필수) 실제 클라이언트에게도 401 응답을 보내야 합니다.
            response.setStatus(customException.getBadStatusCode().getHttpStatus().value());
            response.getWriter().write("Error: " + customException.getMessage()); // 간단한 텍스트 응답

        }else {
            // 혹시 모를 다른 인증 예외 (500에 가까움)
            discordService.send5xxNotification(
                authException.getMessage(),
                authException.toString(),
                clientIp,
                requestInfo
            );

            // (필수) 클라이언트에게 401 응답
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
        }

        // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // response.setStatus(HttpStatus.UNAUTHORIZED.value());
        // response.getWriter().write(mapper.writeValueAsString(HttpServletResponse.SC_UNAUTHORIZED));
    }


    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp != null && !clientIp.isBlank()) {
            return clientIp.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
