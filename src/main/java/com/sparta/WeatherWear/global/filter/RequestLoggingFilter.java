package com.sparta.WeatherWear.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;

public class RequestLoggingFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        logger.info("________________________________________________________");
        // 요청 URL 및 쿠키 정보 로그
        logger.info("Request URL: " + httpRequest.getRequestURL());
        logger.info("Request Cookies: " + Arrays.toString(httpRequest.getCookies()));

        // 필터 체인의 다음 필터로 요청을 전달
        chain.doFilter(request, response);
    }
}
