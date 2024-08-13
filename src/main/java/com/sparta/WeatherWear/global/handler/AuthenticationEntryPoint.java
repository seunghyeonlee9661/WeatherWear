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
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 요청 정보와 인증 오류에 대한 자세한 로그 기록
        String requestURI = request.getRequestURI();
        String clientIP = request.getRemoteAddr();
        String method = request.getMethod();
        String queryString = request.getQueryString();

        logger.error("Unauthorized request: URI: {}, Method: {}, QueryString: {}, Client IP: {}",
                requestURI, method, queryString, clientIP);
        logger.error("Authentication error: {}", authException.getMessage(), authException);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("로그인이 필요한 서비스입니다.");
    }
}