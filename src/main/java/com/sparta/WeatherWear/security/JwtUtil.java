package com.sparta.WeatherWear.security;

import com.sparta.WeatherWear.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/*
작성자 : 이승현
JWT 생성, 검증을 맡은 클래스
*/
@Component
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization"; // Header KEY 값
    public static final String REFRESH_HEADER = "Refresh"; // Refresh Token Header KEY 값
    public static final String AUTHORIZATION_KEY = "auth"; // 사용자 권한 값의 KEY
    public static final String BEARER_PREFIX = "Bearer "; // Token 식별자
    //private final long ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000L; // Access Token 만료시간 : 1시간
    private final long ACCESS_TOKEN_VALIDITY = 1 * 1 * 1000L; // Access Token 만료시간 : 1시간
    private final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000L; // Refresh Token 만료시간 : 7일

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    public static final Logger logger = LoggerFactory.getLogger("JWT : "); // 로그 설정

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createAccessToken(User user) {
        Map<String, Object> additionalClaims = new HashMap<>();
        /* 클레임으로정보 추가 가능*/
        additionalClaims.put("nickname", user.getNickname());
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(user.getEmail())) // 사용자 식별자값(ID)
                        .addClaims(additionalClaims) // 추가 클레임
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_VALIDITY)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // 토큰 생성
    public String createRefreshToken(User user) {
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(user.getEmail())) // 사용자 식별자값(ID)
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_VALIDITY)) // 만료 시간
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // JWT Cookie 에 저장
    public void addJwtToCookie(String accessToken,String refreshToken, HttpServletResponse res) {
        try {
            Cookie accessTokenCookie = new Cookie(AUTHORIZATION_HEADER, URLEncoder.encode(accessToken, "utf-8").replaceAll("\\+", "%20")); // Name-Value
            accessTokenCookie.setPath("/");
            accessTokenCookie.setHttpOnly(true);

            Cookie refreshTokenCookie = new Cookie(REFRESH_HEADER, URLEncoder.encode(refreshToken, "utf-8").replaceAll("\\+", "%20")); // Name-Value
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setHttpOnly(true);

            // Response 객체에 Cookie 추가
            res.addCookie(accessTokenCookie);
            res.addCookie(refreshTokenCookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // JWT Cookie 삭제
    public void removeJwtCookie(HttpServletResponse res) {
        Cookie accessTokenCookie = new Cookie(AUTHORIZATION_HEADER, null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(0); // 쿠키 삭제

        Cookie refreshTokenCookie = new Cookie(REFRESH_HEADER, null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(0); // 쿠키 삭제

        res.addCookie(accessTokenCookie);
        res.addCookie(refreshTokenCookie);
    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateToken(String token,HttpServletResponse res) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT t oken, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // Refresh Token을 사용하여 새로운 Access Token을 발급하는 메서드
    public String refreshAccessToken(String refreshToken, User user) {
        String email = getUserInfoFromToken(refreshToken).getSubject();
        if (user != null && email.equals(user.getEmail())) {
            return createAccessToken(user);
        }
        return null;
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req, String tokenType) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenType)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}