package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardCreateRequestDto {

    private String address;
    private Long addressId;
    private String title;
    private String contents;
    @JsonProperty("isPrivate")
    private boolean isPrivate;
    private List<ClothesRequestDTO> tags;

    public BoardCreateRequestDto(String address, Long addressId, String title, String contents, boolean isPrivate, List<ClothesRequestDTO> tags) {
        this.address = address;
        this.addressId = addressId;
        this.title = title;
        this.contents = contents;
        this.isPrivate = isPrivate;
        this.tags = tags;
    }
}
