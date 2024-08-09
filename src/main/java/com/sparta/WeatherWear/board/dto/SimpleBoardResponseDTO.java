package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.clothes.dto.ClothesResponseDTO;
import com.sparta.WeatherWear.weather.dto.SimpleWeatherResponseDTO;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SimpleBoardResponseDTO {

    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt; // 생성일자
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt; // 수정일자
    private String image;
    private SimpleWeatherResponseDTO weather; // 날씨 정보
    @JsonProperty("isPrivate")
    private boolean isPrivate;
    private List<TagResponseDTO> tags;

    public SimpleBoardResponseDTO(Board board){
        this.title = board.getTitle();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.image = board.getBoardImage();
        this.weather =new SimpleWeatherResponseDTO(board.getWeather());
        this.isPrivate = board.isPrivate();
        this.tags = board.getBoardTags().stream().map(TagResponseDTO::new).collect(Collectors.toList());
    }
}
