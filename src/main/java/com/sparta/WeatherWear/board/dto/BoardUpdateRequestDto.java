package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.board.entity.Comment;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardUpdateRequestDto {

    private Long boardId;
    private Long userId;
    private Long addressId;
    //
    private String title;
    private String content;
    private boolean isPrivate;
    //
    private ClothesColor color;
    private ClothesType type;
}
