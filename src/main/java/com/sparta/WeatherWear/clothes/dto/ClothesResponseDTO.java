package com.sparta.WeatherWear.clothes.dto;

import com.sparta.WeatherWear.global.dto.ResponseDTO;
import com.sparta.WeatherWear.clothes.entity.Clothes;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import lombok.Getter;
/*
작성자 : 이승현
옷 데이터 반환 DTO
 */
@Getter
public class ClothesResponseDTO implements ResponseDTO {
    private Long id;
    private ClothesColor color;
    private ClothesType type;
    private String image;

    public ClothesResponseDTO(Clothes clothes) {
        this.id = clothes.getId();
        this.color = clothes.getColor();
        this.type = clothes.getType();
        this.image = clothes.getImage();
    }
}
