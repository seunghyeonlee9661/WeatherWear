package com.sparta.WeatherWear.global.security;

import com.sparta.WeatherWear.global.service.RedisService;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    public static final String BEARER_PREFIX = "Bearer "; // Token 식별자
    private final long ACCESS_TOKEN_VALIDITY = 60 * 60 * 1000L; // Access Token 만료시간 : 1시간

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    public static final Logger logger = LoggerFactory.getLogger("JWT : "); // 로그 설정


    @Autowired
    private RedisService redisService; // RedisService 주입

    private final UserRepository userRepository;
    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Access 토큰 생성
    public String createAccessToken(User user) {
        Map<String, Object> additionalClaims = new HashMap<>();
        /* 클레임으로정보 추가 가능*/
        additionalClaims.put("nickname", user.getNickname());
        Date date = new Date();
        return BEARER_PREFIX + Jwts.builder()
                        .setSubject(String.valueOf(user.getEmail())) // 사용자 식별자값(ID)
                        .addClaims(additionalClaims) // 추가 클레임
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_VALIDITY)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // Refresh 토큰 생성
    public String createRefreshToken(User user) {
        Date date = new Date();
        return Jwts.builder()
                        .setSubject(String.valueOf(user.getEmail())) // 사용자 식별자값(ID)
                        .setExpiration(new Date(date.getTime() + RedisService.REFRESH_TOKEN_VALIDITY)) // 만료 시간
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // JWT Cookie에 AccessToken 저장
    public void addTokenToCookie(String accessToken, HttpServletResponse res) {
        try {
            Cookie accessTokenCookie = new Cookie(AUTHORIZATION_HEADER, URLEncoder.encode(accessToken, "utf-8").replaceAll("\\+", "%20")); // Name-Value
            accessTokenCookie.setPath("/");
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true); // HTTPS에서만 전송
            accessTokenCookie.setAttribute("SameSite", "None"); // 외부 도메인에서도 쿠키를 전송
            res.addCookie(accessTokenCookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // Redis에 AccessToken과 RefreshToken 저장
    public void addTokenToRedis(String accessToken, String refreshToken) {
        String strippedAccessToken = substringToken(accessToken); // BEARER_PREFIX 제거
        redisService.save(RedisService.REFRESH_TOKEN_PREFIX,strippedAccessToken, refreshToken, RedisService.REFRESH_TOKEN_VALIDITY);
    }

    // JWT Cookie 삭제
    public void removeJwtCookie(HttpServletResponse res) {
        Cookie accessTokenCookie = new Cookie(AUTHORIZATION_HEADER, null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true); // HTTPS 환경에서만 전송
        accessTokenCookie.setMaxAge(0); // 쿠키 삭제
        accessTokenCookie.setAttribute("SameSite", "None"); // 외부 도메인에서도 쿠키를 전송
        res.addCookie(accessTokenCookie);
    }

    // JWT 토큰 substring : BEARER 제거해주는 코드
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
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

    // Access Token 만료시 Refresh Token을 찾아 새로운 Access Token을 발급 하는 기능
    public String refreshAccessToken(String accessToken) {
        // 기존 토큰의 값 추출
        String strippedAccessToken = substringToken(accessToken); // BEARER_PREFIX 제거
        // 기존 토큰 값으로부터 Refresh Token을 Redis로부터 찾아옴
        String storedRefreshToken = redisService.get(RedisService.REFRESH_TOKEN_PREFIX,strippedAccessToken);
        // Refresh Token이 올바른지 확인
        if (storedRefreshToken != null && validateToken(storedRefreshToken)) {
            // Refresh Token으로부터 사용자의 정보 추출
            String email = getUserInfoFromToken(storedRefreshToken).getSubject();
            User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
            // 기존 refreshToken 삭제
            redisService.delete(RedisService.REFRESH_TOKEN_PREFIX,strippedAccessToken);
            // 새로운 Access Token 생성
            String newAccessToken = createAccessToken(user);
            // 새로운 Refresh Token 생성
            String newRefreshToken = createRefreshToken(user);
            // Redis에 새로운 Refresh Token 저장
            redisService.save(RedisService.REFRESH_TOKEN_PREFIX,substringToken(newAccessToken), newRefreshToken, RedisService.REFRESH_TOKEN_VALIDITY);
            return newAccessToken;
        }
        return null;
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req, String tokenType) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenType)) return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8); // Encode 되어 넘어간 Value 다시 Decode
            }
        }
        return null;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}