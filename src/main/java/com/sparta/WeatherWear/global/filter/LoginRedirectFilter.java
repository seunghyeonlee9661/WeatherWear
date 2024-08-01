package com.sparta.WeatherWear.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
작성자 : 이승현
로그인한 사용자가 로그인 페이지에 접근한 경우 대처하는 필터 : 이후에 삭제 가능
*/
@Component
public class LoginRedirectFilter extends OncePerRequestFilter {

    private String loginURL = "/login";
    private String redirectURL = "/";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && request.getRequestURI().equals(loginURL)) {
            response.sendRedirect(redirectURL);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
