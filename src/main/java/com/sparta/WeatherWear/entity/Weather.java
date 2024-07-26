package com.sparta.WeatherWear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
/*
날씨 정보 Entity 구버전 - 삭제 부탁드립니다.
 */
@Getter
@Setter
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 날씨 데이터 ID

    @Column(name = "stn", nullable = false)
    private int stn; // 지역 코드

    @Column(name = "ta", nullable = false)
    private double ta; // 기온

    @Column(name = "sky", nullable = false)
    private String sky; // 하늘 상태

    @Column(name = "prep", nullable = false)
    private double prep; // 강수량

    @Column(name = "date", nullable = false)
    private Date date; // 날짜
}
