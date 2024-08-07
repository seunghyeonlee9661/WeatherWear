package com.sparta.WeatherWear.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
작성자 : 이승현
올바른 권한이 아닌 경우에 대한 예외 처리
*/
@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    public static final Logger logger = LoggerFactory.getLogger("AccessDeniedHandler : "); // 로그 설정
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("올바른 권한을 가지고 있지 않습니다.");
    }
}