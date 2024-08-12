package com.sparta.WeatherWear.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
/*
작성자 : 이승현
* Redis Server에 값을 저장, 조회, 삭제하는 기능
*/
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public static final String REFRESH_TOKEN_PREFIX = "refreshToken:";    // 24시간 조회수 제한을 위한 상수
    public static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000L; // Refresh Token 만료시간 : 7일

    public static final String VIEW_LIMIT_PREFIX = "viewCount:";    // 24시간 조회수 제한을 위한 상수
    public static final long VIEW_LIMIT_DURATION = 24 * 60 * 60 * 1000; // 24시간을 밀리초로 표현

    public static final String RESET_CODE_PREFIX = "resetCode:"; // Redis에 저장될 비밀번호 재설정 코드의 접두사
    public static final long RESET_CODE_DURATION = 5 * 60 * 1000; // 5분

    public static final String NAVER_SHOPPING_API_PREFIX = "resetCode:"; // Redis에 저장될 비밀번호 재설정 코드의 접두사
    public static final long NAVER_SHOPPING_API_DURATION = 60 * 60 * 1000; // 5분

    // redis 저장
    public void save(String prefix,String key, String value, long duration) {
        redisTemplate.opsForValue().set(prefix + key, value, duration, TimeUnit.MILLISECONDS);
    }

    // redis value 가져오기
    public String get(String prefix, String key) {
        // 코드가 유효한 경우 이메일을 조회
        return redisTemplate.opsForValue().get(prefix + key);
    }

    // redis 삭제
    public void delete(String prefix,String key) {
        redisTemplate.delete(prefix + key);
    }

    // 조회수 증가
    public boolean incrementViewCount(String userIp, String boardId) {
        // NX 옵션으로 키가 없을 때만 값을 설정하고, EX 옵션으로 만료 시간을 설정
        Boolean result = redisTemplate.opsForValue().setIfAbsent(VIEW_LIMIT_PREFIX + userIp + ":" + boardId, "viewed", VIEW_LIMIT_DURATION, TimeUnit.MILLISECONDS);
        return result != null && result;
    }
}
