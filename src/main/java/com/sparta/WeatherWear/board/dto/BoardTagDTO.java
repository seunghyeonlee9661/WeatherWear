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
// 게시물의 태그 아이템을 처리하기 위한 DTO
public class BoardTagDTO {
    private ClothesColor color;
    private ClothesType type;

    public BoardTagDTO(BoardTag boardTag){
        this.color = boardTag.getColor();
        this.type = boardTag.getType();
    }
}