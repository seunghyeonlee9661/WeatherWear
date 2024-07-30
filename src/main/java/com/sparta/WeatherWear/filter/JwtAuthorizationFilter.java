package com.sparta.WeatherWear.filter;

import com.sparta.WeatherWear.entity.User;
import com.sparta.WeatherWear.security.JwtUtil;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import com.sparta.WeatherWear.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
작성자 : 이승현
필요에 따라 JWT를 검증
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
        log.info("검증 시작 : " + req.getRequestURI());
        // 액세스 토큰과 리프레시 토큰을 쿠키에서 가져옴
        String accessToken  = jwtUtil.getTokenFromRequest(req, JwtUtil.AUTHORIZATION_HEADER);
        String refreshToken = jwtUtil.getTokenFromRequest(req, JwtUtil.REFRESH_HEADER);

        if (accessToken != null) { // accessToken 확인
            String accessTokenValue = jwtUtil.substringToken(accessToken); // accessToken 검증
            if (jwtUtil.validateToken(accessTokenValue, res)) { // 토큰이 올바르면 사용자 정보 확인
                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.getUserInfoFromToken(accessTokenValue).getSubject()); // 토큰에서 사용자 정보를 추출
                setAuthentication(userDetails,req);  // 사용자 인증 설정
            } else if (refreshToken != null) { // refreshToken으로 재발급 진행
                String refreshTokenValue = jwtUtil.substringToken(refreshToken);
                if(jwtUtil.validateToken(refreshTokenValue, res)){
                    // 리프레시 토큰이 유효한 경우
                    UserDetails userDetails = userDetailsService.loadUserByUsername( jwtUtil.getUserInfoFromToken(refreshTokenValue).getSubject());
                    if (userDetails != null) {
                        User user = ((UserDetailsImpl) userDetails).getUser();
                        String newAccessToken = jwtUtil.refreshAccessToken(refreshTokenValue, user);
                        if (newAccessToken != null) {
                            jwtUtil.addJwtToCookie(newAccessToken, refreshToken, res);// 새로운 액세스 토큰을 쿠키에 추가
                            setAuthentication(userDetails,req); // 사용자 인증 설정
                        }
                    }
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