package com.sparta.WeatherWear.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.WeatherWear.user.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/kakao")
public class KakaoLoginController {
    private final KakaoLoginService kakaoLoginService;

    /* 카카오 로그인 콜백 처리 */
    @GetMapping("/login")
    public ResponseEntity<String> kakaoLogin(@RequestBody Map<String, String> payload, HttpServletResponse response) throws JsonProcessingException {
        String code = payload.get("code");
        return kakaoLoginService.kakaoLogin(code,response);
    }
}
