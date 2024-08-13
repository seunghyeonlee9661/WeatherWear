package com.sparta.WeatherWear.global.filter;

import com.sparta.WeatherWear.global.security.JwtUtil;
import com.sparta.WeatherWear.global.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/*
작성자 : 이승현
JWT를 검증하고 사용자가 로그인한 상태인지 확인하는 필터
*/
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /* 사용자의 로그인 상태를 토큰을 통해 검증합니다. 또한 RefreshToken을 활용해 토큰 탈취에 대비합니다. */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String accessToken  = jwtUtil.getTokenFromRequest(req, JwtUtil.AUTHORIZATION_HEADER); // 액세스 토큰과 리프레시 토큰을 쿠키에서 가져옴
        if (accessToken != null) { // accessToken 확인
            String accessTokenValue = jwtUtil.substringToken(accessToken); // accessToken 검증
            if (jwtUtil.validateToken(accessTokenValue)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.getUserInfoFromToken(accessTokenValue).getSubject());// 토큰이 올바르면 사용자 정보 확인
                setAuthentication(userDetails, req);// 사용자 인증 및 정보 저장
            } else {// accessToken이 유효하지 않은 경우
                String newAccessToken = jwtUtil.refreshAccessToken(accessToken); // accessToken 기반으로 RefreshToken을 찾아 재발급을 진행
                if (newAccessToken != null) {
                    // 새 토큰을 기반으로 사용자 정보 확인
                    UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(newAccessToken)).getSubject()); // 새 토큰에서 사용자 정보를 추출
                    jwtUtil.addTokenToCookie(newAccessToken, res);// 새로운 액세스 토큰을 쿠키에 추가
                    setAuthentication(userDetails, req);// 사용자 인증 설정
                    log.info("User = {}",userDetails.getUsername() );
                }
            }
        }
        filterChain.doFilter(req, res);
    }

    // 사용자 인증 설정
    private void setAuthentication(UserDetails userDetails, HttpServletRequest req){
        if (userDetails != null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}