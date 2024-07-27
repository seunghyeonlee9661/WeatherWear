package com.sparta.WeatherWear.dto.board;

import com.sparta.WeatherWear.dto.ResponseDTO;
import com.sparta.WeatherWear.entity.*;
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
    private Date registDate;
    private Date updateDate;
    private Weather weather;
    private String image;
    private int likeCount;

    public BoardResponseDTO(Board board) {
        this.id = board.getId();
        this.user = board.getUser();
        this.title = board.getTitle();
        this.content = board.getTitle();
        this.isPrivate = board.isPrivate();
        this.registDate = board.getRegistDate();
        this.updateDate = board.getUpdateDate();
        this.weather = board.getWeather();
        this.image = board.getImage();
        this.likeCount =  board.getBoardLikes().size();
    }
}
