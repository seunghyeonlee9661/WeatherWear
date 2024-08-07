package com.sparta.WeatherWear.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
작성자 : 이승현
로그인없이 접근하는 경우에 대한 예외 처리
*/
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    public static final Logger logger = LoggerFactory.getLogger("AuthenticationEntryPoint : "); // 로그 설정
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 로그에 오류 발생 정보 기록
        String requestURI = request.getRequestURI(); // 요청 URI
        String requestMethod = request.getMethod(); // 요청 메서드
        String errorMessage = authException.getMessage(); // 예외 메시지
        Throwable cause = authException.getCause(); // 예외의 원인

        // 자세한 오류 정보를 로그에 기록
        logger.info("Authentication error occurred:");
        logger.info("Request URI: {}", requestURI);
        logger.info("Request Method: {}", requestMethod);
        logger.info("Error Message: {}", errorMessage);
        if (cause != null) {
            logger.info("Cause: {}", cause);
        }

        // 응답 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("로그인이 필요한 서비스입니다.");
    }
}
