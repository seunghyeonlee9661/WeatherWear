package com.sparta.WeatherWear.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.user.dto.UserLoginRequestDTO;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.global.security.JwtUtil;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/*
작성자 : 이승현
로그인을 확인하고 JWT를 생성, 쿠키와 Redis에 토큰을 저장하는 필터
*/
@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    /* 로그인 과정 진행 위치 */
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/login");
    }

    /* 로그인 진행 및 JWT 토큰 반환 */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            UserLoginRequestDTO requestDto = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestDTO.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /* 로그인 성공 */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult){
        log.info("로그인 성공 및 JWT 생성");
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
        // accessToken 생성 및 쿠키 추가
        String accessToken = jwtUtil.createAccessToken(user);
        // RefreshToken 생성 및 Redis 추가
        String refreshToken = jwtUtil.createRefreshToken(user);
        jwtUtil.addTokenToRedis(accessToken,refreshToken);
        jwtUtil.addTokenToCookie(accessToken,response);
    }

    /* 로그인 실패 */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        log.info("로그인 실패");
        response.setStatus(401);
    }
}