package com.sparta.WeatherWear.global.filter;

import com.sparta.WeatherWear.global.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class UserDetailsFilter extends OncePerRequestFilter {

    private final String urlPattern;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public UserDetailsFilter(String urlPattern, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.urlPattern = urlPattern;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // URL 패턴에 해당하는 요청인 경우에만 필터를 적용
        if (requestURI.matches(urlPattern.replace("**", ".*"))) {
            log.info("Processing request URI: {}", requestURI); // 로그 추가
            String accessToken = jwtUtil.getTokenFromRequest(request, JwtUtil.AUTHORIZATION_HEADER);
            if (accessToken != null) {
                String tokenValue = jwtUtil.substringToken(accessToken);

                // 토큰 검증
                if (jwtUtil.validateToken(tokenValue)) {
                    // 사용자 정보 가져오기
                    UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.getUserInfoFromToken(tokenValue).getSubject());
                    if (userDetails != null) {
                        setAuthentication(userDetails, request);
                    }
                } else {
                    // 토큰이 만료된 경우 새 액세스 토큰 생성
                    String newAccessToken = jwtUtil.refreshAccessToken(accessToken);
                    if (newAccessToken != null) {
                        jwtUtil.addTokenToCookie(newAccessToken, response);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.getUserInfoFromToken(newAccessToken).getSubject());
                        setAuthentication(userDetails, request);
                        log.info("New Access Token issued for user: {}", userDetails.getUsername());
                    }
                }
            }
        } else {
            log.info("Request URI does not match URL pattern: {}", requestURI);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        if (userDetails != null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication set for user: {}", userDetails.getUsername());
        }
    }
}
