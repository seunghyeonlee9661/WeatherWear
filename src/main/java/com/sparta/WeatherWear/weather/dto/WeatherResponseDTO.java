package com.sparta.WeatherWear.weather.dto;

import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;

@Getter
public class WeatherResponseDTO {
    private Long id;
    private Double POP;  // 강수확률 : %
    private int PTY;  // 강수형태 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
    private Double PCP;  // 1시간 강수량 (mm)
    private Double REH;  // 습도 : %
    private Double SNO;  // 1 시간 신적설 (cm)
    private int SKY;  // 하늘상태 : 맑음(1), 구름많음(3), 흐림(4)
    private Double TMP;  // 1 시간 기온 (°C)
    private Double TMN;  // 일 최저기온 (°C)
    private Double TMX;  // 일 최고기온 (°C)
    private Double UUU;  // 풍속(동서성분) (m/s)
    private Double VVV;  // 풍속(남북성분) (m/s)
    private Double WAV;  // 파고 (M)
    private Double VEC;  // 풍향 (deg)
    private Double WSD;  // 풍속 (m/s) : -4(약)/4-8.9(약간강)/9-13.9(강)/14-(매우강)

    public WeatherResponseDTO(Weather weather) {
        this.id = weather.getId();;
        this.POP = weather.getPOP();
        this.PTY = weather.getPTY();
        this.PCP = weather.getPCP();
        this.REH = weather.getREH();
        this.SNO = weather.getSNO();
        this.SKY = weather.getSKY();
        this.TMP = weather.getTMP();
        this.TMN = weather.getTMN();
        this.TMX = weather.getTMX();
        this.UUU = weather.getUUU();
        this.VVV = weather.getVVV();
        this.WAV = weather.getWAV();
        this.VEC = weather.getVEC();
        this.WSD = weather.getWSD();
    }
}
