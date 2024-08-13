package com.sparta.WeatherWear.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.user.dto.KakaoUserResponseDto;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.user.repository.UserRepository;
import com.sparta.WeatherWear.global.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;
/*
작성자 : 이승현
카카오 로그인 서비스 처리 기능
*/
@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    @Value("${kakao.client.id}")
    private String client_id;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public ResponseEntity<String> kakaoLogin(String code,String redirectUri, HttpServletResponse response) throws JsonProcessingException {
        // "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code,redirectUri);
        // 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserResponseDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 카카오 유저를 찾아서 없을 경우 회원 가입 진행
        User kakaoUser = registerKakaoUser(kakaoUserInfo);
        // JWT 토큰 쿠키에 추가
        String jwtAccessToken = jwtUtil.createAccessToken(kakaoUser);
        jwtUtil.addTokenToCookie(jwtAccessToken,response);
        String jwtRefreshToken = jwtUtil.createRefreshToken(kakaoUser);
        jwtUtil.addTokenToRedis(jwtAccessToken,jwtRefreshToken);
        return ResponseEntity.ok("Kakao login successfully");
    }

    // 카카오 회원가입 진행
    private User registerKakaoUser(KakaoUserResponseDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();
                String image = kakaoUserInfo.getImage();
                kakaoUser = new User(encodedPassword, email,kakaoUserInfo.getNickname()+"_"+kakaoId, kakaoId,image);
            }
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }


    // 카카오 토큰 처리
    private String getToken(String code,String redirectUri) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com").path("/oauth/token").encode().build().toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserResponseDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder.fromUriString("https://kapi.kakao.com").path("/v2/user/me").encode().build().toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity.post(uri).headers(headers).body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(requestEntity,String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        Long id = jsonNode.get("id").asLong();
        // nickname 처리
        JsonNode propertiesNode = jsonNode.get("properties");
        String nickname = propertiesNode != null && propertiesNode.get("nickname") != null ? propertiesNode.get("nickname").asText() : "No Nickname";
        // email 처리
        JsonNode kakaoAccountNode = jsonNode.get("kakao_account");
        String email = kakaoAccountNode != null && kakaoAccountNode.get("email") != null ? kakaoAccountNode.get("email").asText() : "No Email";
        // profile_image 처리
        String image = propertiesNode != null && propertiesNode.get("profile_image") != null ? propertiesNode.get("profile_image").asText() : "No Image";
        return new KakaoUserResponseDto(id, nickname, email,image);
    }
}