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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/weathers") // 전체 날씨 조회
    public ResponseEntity<WeatherNew> getWeatherByCoordinate(@RequestParam(value = "x") double x, @RequestParam(value = "y") double y) throws JsonProcessingException {
        return weatherService.getWeatherByCoordinate(x, y);
    }

}
