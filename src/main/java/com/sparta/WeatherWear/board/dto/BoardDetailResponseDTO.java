package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.board.entity.Comment;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.user.dto.SimpleUserDTO;
import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/*
 * 작성자 : 이승현
 * 게시물 상세보기를 위한 DTO
 * */
@Getter
@Setter
public class BoardDetailResponseDTO {

    private long id;
    private SimpleUserDTO user; // 사용자 정보
    private String address; // 즈소
    private Weather weather;
    private String title;
    private String contents;
    private List<BoardTagDTO> tags;
    private String image;
    private int views;
    private int likeCount;
    private List<SimpleCommentDTO> comments;
    private int commentCount;
    private boolean isLike;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String registDate;

    public BoardDetailResponseDTO(Board board) {
        this.id = board.getId();
        this.user = new SimpleUserDTO(board.getUser());
        this.address = board.getAddress();
        this.weather = board.getWeather();
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.tags = board.getTags().stream().map(BoardTagDTO::new).collect(Collectors.toList()); // BoardTagDTO 생성자 사용
        this.image = board.getImage();
        this.views = board.getViews();
        this.commentCount = board.getCommentsSize();
        this.likeCount = board.getLikesSize();
        this.comments = board.getComments().stream().map(SimpleCommentDTO::new).collect(Collectors.toList());
        this.registDate = board.getRegistDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    }
}
