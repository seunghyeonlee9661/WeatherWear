package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 추가
public class BoardUpdateRequestDto {

    private Long Id;
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
