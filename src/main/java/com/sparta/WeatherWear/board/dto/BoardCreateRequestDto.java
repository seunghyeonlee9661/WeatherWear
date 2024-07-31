package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.BoardImage;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.entity.Weather;
import com.sparta.WeatherWear.enums.ClothesColor;
import com.sparta.WeatherWear.enums.ClothesType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardCreateRequestDto {

//    private Long userId;
    private String title;
    private String contents;
    private boolean isPrivate;
    private int stn;
    private ClothesColor color;
    private ClothesType type;
}
