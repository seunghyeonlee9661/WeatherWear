package com.sparta.WeatherWear.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.entity.Address;
import com.sparta.WeatherWear.entity.Weather;
import com.sparta.WeatherWear.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private static final Logger logger = Logger.getLogger(WeatherService.class.getName());

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final WeatherRepository weatherRepository;
    private final KakaoMapService kakaoMapService;

    @Transactional
    public ResponseEntity<String> getWeather(double x, double y) throws JsonProcessingException {
        Address address = kakaoMapService.findAddress(x, y); // 좌표로 지역 정보를 불러옵니다.
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS); // 현재 시간을 분 단위로 내림 처리하여 가져옵니다.
        Weather weather = weatherRepository.findByStnAndDate(address.getCode(), now) // Weather 데이터를 조회합니다.
                .orElseGet(() -> { // Weather가 없는 경우 기상청 API를 통해 값을 찾아옵니다.
                    Weather newWeather = getWeatherByAPI(address.getNx(),address.getNy(), now);
                    weatherRepository.save(newWeather);
                    return newWeather;
                });
        return ResponseEntity.ok("");
    }

    private Weather getWeatherByAPI(int nx, int ny, LocalDateTime date){
        // 기상청 API 처리 하는 로직
        return null;
    }
}
