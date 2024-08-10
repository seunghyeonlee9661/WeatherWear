package com.sparta.WeatherWear.user.dto;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.global.dto.ResponseDTO;
import lombok.Getter;

import java.util.List;

/*
작성자 : 이승현
옷 데이터 반환 DTO
 */
@Getter
public class RecommendBoardResponseDTO implements ResponseDTO {
    private Long id;
    private String image;

    public RecommendBoardResponseDTO(Board board) {
        this.id = board.getId();
        this.image = board.getBoardImage();
    }
}
