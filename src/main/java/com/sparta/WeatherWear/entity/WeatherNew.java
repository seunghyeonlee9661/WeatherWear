package com.sparta.WeatherWear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/*
작성자 : 이승현
날씨 정보 캐시 Entity
 */
@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
@Table(name="weather_new")
public class WeatherNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "pty")
    private Double pty;  // 강수형태

    @Column(name = "reh")
    private Double reh;  // 습도

    @Column(name = "rn1")
    private Double rn1;  // 1시간 강수량

    @Column(name = "t1h")
    private Double t1h;  // 기온

    @Column(name = "uuu")
    private Double uuu;  // 동서성분의 바람속도

    @Column(name = "vec")
    private Double vec;  // 풍향

    @Column(name = "vvv")
    private Double vvv;  // 남북성분의 바람속도

    @Column(name = "wsd")
    private Double wsd;  // 풍속

    // 매개변수 있는 생성자
    public WeatherNew(String baseDate, String baseTime, Address address, Double pty, Double reh, Double rn1, Double t1h, Double uuu, Double vec, Double vvv, Double wsd) throws ParseException {
        this.pty = pty;
        this.reh = reh;
        this.rn1 = rn1;
        this.t1h = t1h;
        this.uuu = uuu;
        this.vec = vec;
        this.vvv = vvv;
        this.wsd = wsd;
        this.date = new java.text.SimpleDateFormat("yyyyMMddHHmm").parse(baseDate + baseTime);
        this.address = address;
    }
}
