package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.global.dto.ResponseDTO;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;

import java.util.Date;

/*
작성자 : 이승현
옷 데이터 반환 DTO
 */
@Getter
public class BoardResponseDTO implements ResponseDTO {
    private Long id;
    private User user;
    private String title;
    private String content;
    private boolean isPrivate;
    private Weather weather;
    private int likeCount;
    private int views;

    public BoardResponseDTO(Board board) {
        this.id = board.getId();
        this.user = board.getUser();
        this.title = board.getTitle();
        this.content = board.getTitle();
        this.isPrivate = board.isPrivate();
        this.weather = board.getWeather();
        this.likeCount =  board.getBoardLikes().size();
        this.views = board.getViews();
    }
}
