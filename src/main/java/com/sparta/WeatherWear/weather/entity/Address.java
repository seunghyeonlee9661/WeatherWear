package com.sparta.WeatherWear.weather.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
/* 행정동 코드와 기상청 api 격자 값 맵핑을 위한 엔티티 */
@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
public class Address {
    @Id
    @Column(name = "code", nullable = false)
    private Long id; // 행정구역 코드

    @Column(name = "nx", nullable = false)
    private int nx; // 격자X

    @Column(name = "ny", nullable = false)
    private int ny; // 격자Y
}
