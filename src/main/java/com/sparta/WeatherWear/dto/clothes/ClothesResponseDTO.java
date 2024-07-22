package com.sparta.WeatherWear.dto.clothes;

import com.sparta.WeatherWear.entity.Clothes;
import com.sparta.WeatherWear.enums.ClothesColor;
import com.sparta.WeatherWear.enums.ClothesType;
import lombok.Getter;

@Getter
public class ClothesResponseDTO {
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
