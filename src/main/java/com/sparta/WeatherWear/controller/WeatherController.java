package com.sparta.WeatherWear.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.WeatherWear.dto.ResponseDTO;
import com.sparta.WeatherWear.entity.Weather;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import com.sparta.WeatherWear.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
작성자 : 이승현
날씨 관련 요청을 처리하는 API Controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WeatherController {
    private final WeatherService weatherService;

    /* 주소값을 통해 날씨 정보를 받아올 수 있도록 요청을 처리합니다.*/
    /* 해당 기능은 주소값 호출에 대한 테스트를 위해 구현되었습니다. */
    /* 하부 기능은 추천 시스템에서 본격적으로 사용합니다. */
    @GetMapping("/weathers") // 전체 날씨 조회
    public ResponseEntity<Weather> getWeatherByAddress(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(weatherService.getWeatherByAddress(id));
    }

//    /* 추천 아이템들 불러오기 */
//    @GetMapping("/weathers/random")
//    public ResponseEntity<String> getRecommend(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return ResponseEntity.ok(weatherService.getWeatherByAddress());
//    }
}
