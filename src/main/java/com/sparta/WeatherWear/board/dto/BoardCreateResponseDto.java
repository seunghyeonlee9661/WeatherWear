package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardImage;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.board.entity.Comment;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
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
    // 사용자 정보
    private Long userId;
    private String nickname;
    private String image;
    //
    private String title;
    private String contents;
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
    private ClothesColor clothesColor;
    private ClothesType clothesType;
    private List<String> boardImages;
    private int views;

    // 게시물 찾을 때
    public BoardCreateResponseDto(Board board) {
        this.id = board.getId();
        // 사용자
        this.userId = board.getUser().getId();
        this.nickname = board.getUser().getNickname();
        this.image = board.getUser().getImage();
        // 게시물
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
        this.registDate = board.getRegistDate();
        this.updateDate = board.getUpdateDate();
        // 날씨
        this.weather = board.getWeather();
        this.addr = board.getAddr();
        //
        this.boardLikes = board.getLikesSize();
        this.commentsSize = board.getCommentsSize();
        //
        List<BoardTag> boardTags= board.getBoardTags();
        for (BoardTag boardTag : boardTags) {
            this.clothesColor = boardTag.getColor();
            this.clothesType = boardTag.getType(); 
        }
        //
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = board.getViews();
    }
    // 처음 생성할 때
    public BoardCreateResponseDto(Board board, List<ClothesRequestDTO> clothesRequestDTO) {
        this.id = board.getId();
        // 사용자
        this.userId = board.getUser().getId();
        this.nickname = board.getUser().getNickname();
        this.image = board.getUser().getImage();
        // 게시물
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
        this.registDate = board.getRegistDate();
        this.updateDate = board.getUpdateDate();
        // 날씨
        this.weather = board.getWeather();
        this.addr = board.getAddr();
        //
        this.boardLikes = board.getLikesSize();
        this.commentsSize = board.getCommentsSize();

        //
        this.clothesRequestDTO = clothesRequestDTO;
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = board.getViews();
    }

    // 게시물 조회 & 조회수 추가
    public BoardCreateResponseDto(Board board, int views, List<ClothesRequestDTO> clothesRequestDTO) {
        this.id = board.getId();
        // 사용자
        this.userId = board.getUser().getId();
        this.nickname = board.getUser().getNickname();
        this.image = board.getUser().getImage();
        // 게시물
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
        this.registDate = board.getRegistDate();
        this.updateDate = board.getUpdateDate();
        // 날씨
        this.weather = board.getWeather();
        this.addr = board.getAddr();
        //
        this.boardLikes = board.getLikesSize();
        this.commentsSize = board.getCommentsSize();
        //
        this.clothesRequestDTO = clothesRequestDTO;
        //
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = views;
    }
}
