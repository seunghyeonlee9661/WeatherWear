package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import lombok.Getter;

@Getter
public class TagResponseDTO {
    private ClothesColor color;
    private ClothesType type;

    public TagResponseDTO(BoardTag boardTag) {
        this.color = boardTag.getColor();
        this.type = boardTag.getType();
    }
}
