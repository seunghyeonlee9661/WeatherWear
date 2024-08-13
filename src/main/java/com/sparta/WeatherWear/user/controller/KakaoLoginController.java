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
        // 프론트엔드에서 받은 "code" 값을 가져옴
        String code = payload.get("code");
        String redirectUri = payload.get("redirectUri");

        // 로그로 code 값을 확인
        System.out.println("Received code: " + code);
        System.out.println("Received code: " + redirectUri);

        // code가 null일 경우 예외 처리
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Authorization code is missing or empty.");
        }
        // 서비스 메서드 호출
        return kakaoLoginService.kakaoLogin(code,redirectUri, response);
    }
}
