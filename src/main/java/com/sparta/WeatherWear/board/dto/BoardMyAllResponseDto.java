package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardMyAllResponseDto {

    private long id;
    //
    private String title;
    private boolean isPrivate;
    private LocalDateTime registDate;
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
    
    //FIXME : Board 값을 받아 처리할 수 있도록 업데이트
    public BoardMyAllResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.isPrivate = board.isPrivate();
        this.registDate = board.getCreatedAt();
        this.updateDate = board.getUpdatedAt();
        this.weather = board.getWeather();
        this.addr = board.getAddress();
        this.boardLikes = board.getBoardLikes().size();
        this.commentsSize = board.getCommentsSize();
        // this.clothesRequestDTO = board.; //FIXME : 이 부분 리스트로 넣으시나요 아니면 하나만 넣어시나요
        // this.boardImages = board.getBoardImages(); //FIXME : 게시물 이미지는 어떻게 전달하시나요
        this.views = board.getViews();
    }
}
