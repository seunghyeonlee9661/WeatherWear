package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;

import lombok.Getter;

import java.util.List;

@Getter
public class BoardCreateRequestDto {

    private String address;
    private Long addressId;
    //
    private String title;
    private String contents;

    @JsonProperty("isPrivate")
    private boolean isPrivate;
    //
    private List<ClothesRequestDTO> tags;

}
