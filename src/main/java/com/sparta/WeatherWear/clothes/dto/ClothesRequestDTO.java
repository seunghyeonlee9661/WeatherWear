package com.sparta.WeatherWear.clothes.dto;

import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
작성자 : 이승현
옷 데이터 요청 DTO
 */
@Getter
@NoArgsConstructor
public class ClothesRequestDTO {
    private ClothesColor color;
    private ClothesType type;

    public ClothesRequestDTO(ClothesColor color, ClothesType type) {
        this.color = color;
        this.type = type;
    }
}

