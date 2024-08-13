package com.sparta.WeatherWear.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.WeatherWear.user.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth/kakao")
public class KakaoLoginController {

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;

    private final KakaoLoginService kakaoLoginService;

    /* 카카오 로그인 콜백 처리 */
    @GetMapping("/login")
    public void kakaoLogin(HttpServletResponse response) throws IOException {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUri
                + "&response_type=code";
        response.sendRedirect(kakaoLoginUrl);
    }
    /* 카카오 로그인 콜백 처리 */
    @GetMapping("/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoLoginService.kakaoLogin(code,response);
    }
}
