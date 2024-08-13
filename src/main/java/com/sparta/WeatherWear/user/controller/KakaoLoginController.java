package com.sparta.WeatherWear.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.WeatherWear.user.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Tag(name = "카카오 API", description = "카카오 로그인 API")
@RequestMapping("/api/kakao")
public class KakaoLoginController {
    private final KakaoLoginService kakaoLoginService;

    /* 카카오 로그인 콜백 처리 */
    @PostMapping("/login")
    public ResponseEntity<String> kakaoLogin(@RequestBody Map<String, String> payload, HttpServletResponse response) throws JsonProcessingException {
        String code = payload.get("code");
        String redirectUri = payload.get("redirectUri");
        return kakaoLoginService.kakaoLogin(code,redirectUri, response);
    }
}
