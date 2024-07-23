package com.sparta.WeatherWear.dto.clothes;

import com.sparta.WeatherWear.enums.ClothesColor;
import com.sparta.WeatherWear.enums.ClothesType;
import lombok.Getter;

@Getter
public class ClothesRequestDTO {
    private ClothesColor color;
    private ClothesType type;
    private String image;
}
