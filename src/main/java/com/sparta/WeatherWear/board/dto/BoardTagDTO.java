package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor
public class BoardTagDTO {
    private ClothesColor color;
    private ClothesType type;

    public BoardTagDTO(BoardTag boardTag){
        this.color = boardTag.getColor();
        this.type = boardTag.getType();
    }
}