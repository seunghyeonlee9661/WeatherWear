package com.sparta.WeatherWear.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.io.IOException;

/*
작성자 : 이승현
로그인없이 접근하는 경우에 대한 예외 처리
*/
@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    public static final Logger logger = LoggerFactory.getLogger("AuthenticationEntryPoint : "); // 로그 설정
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 직렬화

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

// 상세 오류 정보를 JSON 형식으로 응답에 포함
        ErrorResponse errorResponse = new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요한 서비스입니다.", errorMessage, cause != null ? cause.toString() : null);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    // 오류 응답을 위한 내부 클래스
    private static class ErrorResponse {
        private int status;
        private String message;
        private String error;
        private String cause;

        public ErrorResponse(int status, String message, String error, String cause) {
            this.status = status;
            this.message = message;
            this.error = error;
            this.cause = cause;
        }

        // getters and setters (생략, Lombok 등을 사용할 수 있음)
        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getCause() {
            return cause;
        }

        public void setCause(String cause) {
            this.cause = cause;
        }
    }
}
