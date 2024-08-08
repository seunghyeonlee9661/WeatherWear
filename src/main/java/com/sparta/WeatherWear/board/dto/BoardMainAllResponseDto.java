package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardMainAllResponseDto {

    private long boardId;
    //
    private String title;
    private boolean isPrivate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updateDate;
    //
    private Weather weather;
    private String addr;
    //
    private int boardLikes;
    //
    private int commentsSize;
    //
    private List<ClothesRequestDTO> clothesRequestDTO;
    private List<String> boardImages;
    private int views;

}
