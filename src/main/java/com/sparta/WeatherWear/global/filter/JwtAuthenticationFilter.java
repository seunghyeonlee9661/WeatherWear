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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
        try {
            // 로그: 로그인 시도
            log.info("로그인 시도: {}", request.getRequestURI());

            // 요청에서 로그인 정보를 읽어와 DTO로 변환
            UserLoginRequestDTO requestDto = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestDTO.class);

            // 로그: 사용자 정보
            log.info("로그인 요청 사용자: {}", requestDto.getUsername());

            // 인증 처리
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            // 로그: 로그인 처리 중 오류
            log.error("로그인 처리 중 오류: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /* 로그인 성공 */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();

        // 로그: 로그인 성공
        log.info("로그인 성공: 사용자 - {}", user.getNickname());

        // AccessToken 및 RefreshToken 생성
        String accessToken = jwtUtil.createAccessToken(user);
        String refreshToken = jwtUtil.createRefreshToken(user);

        // 로그: JWT 토큰 생성
        log.info("JWT 토큰 생성: AccessToken - {}, RefreshToken - {}", accessToken, refreshToken);

        // Redis 및 쿠키에 토큰 저장
        jwtUtil.addTokenToRedis(accessToken, refreshToken);
        jwtUtil.addTokenToCookie(accessToken, response);

        // 성공 응답 전송
        sendResponse(response, HttpStatus.OK, "로그인 성공");
    }

    /* 로그인 실패 */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // 로그: 로그인 실패
        log.warn("로그인 실패: {}", failed.getMessage());

        // 실패 응답 전송
        sendResponse(response, HttpStatus.UNAUTHORIZED, "로그인 실패: " + failed.getMessage());
    }

    /* 로그인 결과에 대해 응답 전송 */
    private void sendResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", message);

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        objectMapper.writeValue(out, responseBody);
        out.flush();

        // 로그: 응답 전송
        log.info("응답 전송: 상태 - {}, 메시지 - {}", status, message);
    }
}
