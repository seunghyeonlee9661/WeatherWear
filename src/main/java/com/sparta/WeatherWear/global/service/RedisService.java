package com.sparta.WeatherWear.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
/*
* Redis Server에 값을 저장, 조회, 삭제하는 기능
*/

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";    // 24시간 조회수 제한을 위한 상수
    private static final long VIEW_LIMIT_DURATION = 24 * 60 * 60 * 1000; // 24시간을 밀리초로 표현

    private static final String RESET_CODE_PREFIX = "resetCode:"; // Redis에 저장될 비밀번호 재설정 코드의 접두사
    private static final long RESET_CODE_EXPIRATION = 5 * 60 * 1000; // 5분


    // Refresh Token 저장
    public void saveRefreshToken(String accessToken, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + accessToken, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
    }

    // 비밀번호 재설정 코드와 이메일을 저장
    public void saveEmailResetToken(String email, String code) {
        redisTemplate.opsForValue().set(RESET_CODE_PREFIX + code, email, RESET_CODE_EXPIRATION, TimeUnit.SECONDS);
    }

    // 코드로 이메일을 검색
    public String getEmailFromResetToken(String code) {
        // 코드가 유효한 경우 이메일을 조회
        return redisTemplate.opsForValue().get(RESET_CODE_PREFIX + code);
    }

    // 코드로 이메일을 찾아서 반환합니다.
    public String getRefreshToken(String accessToken) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + accessToken);
    }

    // Refresh Token 삭제
    public void deleteRefreshToken(String accessToken) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + accessToken);
    }


    // 조회수 증가
    public boolean incrementViewCount(String userIp, String boardId) {
        String key = "viewCount:" + userIp + ":" + boardId;
        // NX 옵션으로 키가 없을 때만 값을 설정하고, EX 옵션으로 만료 시간을 설정
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "viewed", VIEW_LIMIT_DURATION, TimeUnit.MILLISECONDS);
        // result가 true면 새로운 키가 생성되었으므로 조회수를 증가시킴
        return result != null && result;
    }
}
