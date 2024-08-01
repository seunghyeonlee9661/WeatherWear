package com.sparta.WeatherWear.weather.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.util.Date;

/*
작성자 : 이승현
날씨 정보 Entity
 */
@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
@Table(name="weather")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "pop")
    private Double POP;  // 강수확률 : %

    @Column(name = "pty")
    private int PTY;  // 강수형태 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)

    @Column(name = "pcp")
    private Double PCP;  // 1시간 강수량 (mm)

    @Column(name = "reh")
    private Double REH;  // 습도 : %

    @Column(name = "sno")
    private Double SNO;  // 1 시간 신적설 (cm)

    @Column(name = "sky")
    private int SKY;  // 하늘상태 : 맑음(1), 구름많음(3), 흐림(4)

    @Column(name = "tmp")
    private Double TMP;  // 1 시간 기온 (°C)

    @Column(name = "tmn")
    private Double TMN;  // 일 최저기온 (°C)

    @Column(name = "tmx")
    private Double TMX;  // 일 최고기온 (°C)

    @Column(name = "uuu")
    private Double UUU;  // 풍속(동서성분) (m/s)

    @Column(name = "vvv")
    private Double VVV;  // 풍속(남북성분) (m/s)

    @Column(name = "wav")
    private Double WAV;  // 파고 (M)

    @Column(name = "vec")
    private Double VEC;  // 풍향 (deg)

    @Column(name = "wsd")
    private Double WSD;  // 풍속 (m/s) : -4(약)/4-8.9(약간강)/9-13.9(강)/14-(매우강)

    public Weather(String baseDate, String baseTime, Address address, Double POP, int PTY, Double PCP, Double REH, Double SNO, int SKY, Double TMP, Double TMN, Double TMX, Double UUU, Double VVV, Double WAV, Double VEC, Double WSD) throws ParseException {
        this.POP = POP;
        this.PTY = PTY;
        this.PCP = PCP;
        this.REH = REH;
        this.SNO = SNO;
        this.SKY = SKY;
        this.TMP = TMP;
        this.TMN = TMN;
        this.TMX = TMX;
        this.UUU = UUU;
        this.VVV = VVV;
        this.WAV = WAV;
        this.VEC = VEC;
        this.WSD = WSD;
        this.date = new java.text.SimpleDateFormat("yyyyMMddHHmm").parse(baseDate + baseTime);
        this.address = address;
    }
}
