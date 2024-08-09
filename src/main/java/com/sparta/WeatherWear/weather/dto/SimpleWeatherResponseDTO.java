package com.sparta.WeatherWear.weather.dto;

import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;

@Getter
public class SimpleWeatherResponseDTO {
    private Double POP;  // 강수확률 : %
    private int PTY;  // 강수형태 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
    private Double PCP;  // 1시간 강수량 (mm)
    private int SKY;  // 하늘상태 : 맑음(1), 구름많음(3), 흐림(4)
    private Double TMP;  // 1 시간 기온 (°C)
    private Double WSD;  // 풍속 (m/s) : -4(약)/4-8.9(약간강)/9-13.9(강)/14-(매우강)
    private Long addressId; // 법정동 코드

    public SimpleWeatherResponseDTO(Weather weather) {
        this.POP = weather.getPOP();
        this.PTY = weather.getPTY();
        this.PCP = weather.getPCP();
        this.SKY = weather.getSKY();
        this.TMP = weather.getTMP();
        this.WSD = weather.getWSD();
        this.addressId = weather.getAddress().getId();
    }
}