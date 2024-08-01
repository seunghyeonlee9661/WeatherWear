package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardImage;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;

import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardCreateResponseDto {

    private long id;
    private Long userId;
    private String nickname;
    private String title;
    private String contents;
    private boolean isPrivate;
    private LocalDateTime registDate;
    private LocalDateTime updateDate;
    private Weather weather;
    private int boardLikes;
    private int comments;
    private ClothesColor clothesColor;
    private ClothesType clothesType;
    private List<String> boardImages;
    private int views;

    // 게시물 찾을 때
    public BoardCreateResponseDto(Board board) {
        this.id = board.getId();
        this.userId = board.getUser().getId();
        this.nickname = board.getUser().getNickname();
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
        this.registDate = board.getRegistDate();
        this.updateDate = board.getUpdateDate();
        this.weather = board.getWeather();
        this.boardLikes = board.getLikesSize();
        this.comments = board.getCommentsSize();
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = board.getViews();
    }
    // 처음 생성할 때
    public BoardCreateResponseDto(Board board, ClothesColor clothesColor, ClothesType clothesType) {
        this.id = board.getId();
        this.userId = board.getUser().getId();
        this.nickname = board.getUser().getNickname();
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
        this.registDate = board.getRegistDate();
        this.updateDate = board.getUpdateDate();
        this.weather = board.getWeather();
        this.boardLikes = board.getLikesSize();
        this.comments = board.getCommentsSize();
        this.clothesColor = clothesColor;
        this.clothesType = clothesType;
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = board.getViews();
    }

}
