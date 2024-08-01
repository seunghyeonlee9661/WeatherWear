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

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    // Refresh Token 저장
    public void saveRefreshToken(String accessToken, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + accessToken, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
    }

    // Refresh Token 조회
    public String getRefreshToken(String accessToken) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + accessToken);
    }

    // Refresh Token 삭제
    public void deleteRefreshToken(String accessToken) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + accessToken);
    }
}
