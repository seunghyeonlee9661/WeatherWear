package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 추가
public class BoardUpdateRequestDto {

    private Long Id;
//    private Long boardUserId;
    private String address;
    private Long addressId;
    //
    private String title;
    private String contents;

    @JsonProperty("isPrivate")
    private boolean isPrivate;
    //
    private List<ClothesRequestDTO> tags;

    public BoardUpdateRequestDto(Long boardId, String address, Long addressId, String title, String contents, boolean isPrivate, List<ClothesRequestDTO> tags) {
        this.Id = boardId;
        this.addressId = addressId;
        this.title = title;
        this.contents = contents;
        this.isPrivate = isPrivate;
        this.tags = tags;

    }
}
