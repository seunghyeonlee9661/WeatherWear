package com.sparta.WeatherWear.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.WeatherWear.entity.Weather;
import com.sparta.WeatherWear.entity.WeatherNew;
import com.sparta.WeatherWear.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/*
작성자 : 이승현
날씨 관련 요청을 처리하는 API Controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WeatherController {
    private final WeatherService weatherService;
    /* 좌표를 통해 날씨 정보를 받아올 수 있도록 요청을 처리합니다.*/
    @GetMapping("/weathers") // 전체 날씨 조회
    public ResponseEntity<WeatherNew> getWeatherByCoordinate(@RequestParam(value = "x") double x, @RequestParam(value = "y") double y) throws JsonProcessingException {
        return weatherService.getWeatherByCoordinate(x, y);
    }

}
