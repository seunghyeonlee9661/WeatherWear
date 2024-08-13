package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.WeatherWear.board.entity.Board;

import com.sparta.WeatherWear.user.dto.SimpleUserDTO;
import com.sparta.WeatherWear.weather.dto.SimpleWeatherResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class BoardCreateResponseDto  {

    private long id;
    private SimpleUserDTO user; // 사용자 정보
    private String title; // 제목
    private String contents; // 내용
    @JsonProperty("isPrivate")
    private boolean isPrivate; // private 여부

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt; // 생성일자
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt; // 수정일자


    private String address; // 주소
    private SimpleWeatherResponseDTO weather; // 날씨 정보
    //
    private int boardLikesCount; // 좋아요 수
    //
    private List<TagResponseDTO> tags; // 태그 목록
    private String image; // 게시물 이미지
    private int views; // 조회수

    private boolean checkLike; // 현재 사용자의 좋아요 여부
    private int commentsCount; // 댓글 수

    // 게시물 찾을 때
    public BoardCreateResponseDto(Board board) {
        this.id = board.getId();
        // 사용자
        this.user = new SimpleUserDTO(board.getUser());
        // 게시물
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
        //시간
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        // 날씨
        this.weather = new SimpleWeatherResponseDTO(board.getWeather());
        this.address = board.getAddress();
        //
        this.boardLikesCount = board.getLikesSize();
        this.tags = board.getBoardTags().stream().map(TagResponseDTO::new).collect(Collectors.toList());
        //
        this.image = board.getBoardImage();
        this.views = board.getViews();
        //
        this.commentsCount = board.getCommentsSize();
    }
}
