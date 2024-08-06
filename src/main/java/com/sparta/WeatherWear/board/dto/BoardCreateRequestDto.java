package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardCreateRequestDto {

    private String addr;
    private Long bCode;
    //
    private String title;
    private String contents;
    private boolean isPrivate;
    //
    private List<ClothesRequestDTO> clothesRequestDTO;
//    private ClothesColor color;
//    private ClothesType type;
    //
    private int views;

}
