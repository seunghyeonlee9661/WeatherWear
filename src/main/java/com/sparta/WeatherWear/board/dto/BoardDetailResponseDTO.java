package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.user.dto.SimpleUserDTO;
import com.sparta.WeatherWear.weather.dto.WeatherResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/* 게시물 상세 정보에 대한 반환 DTO */
@Getter
@Setter
public class BoardDetailResponseDTO {

    private long id;
    private SimpleUserDTO user; // 사용자 정보
    private String address; // 주소
    private WeatherResponseDTO weather; // 날씨 정보
    private String title; // 제목
    private String contents; // 내용
    private List<BoardTagDTO> tags; // 태그 목록
    private String image; // 이미지
    private int views; // 조회수
    private int likeCount; // 좋아요 수
    private List<SimpleCommentDTO> comments; // 댓글
    private int commentCount; // 댓글 수
    private boolean isLike; // 사용자 좋아요 확인
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String registDate; // 작성일자

    public BoardDetailResponseDTO(Board board) {
        this.id = board.getId();
        this.user = new SimpleUserDTO(board.getUser());
        this.address = board.getAddress();
        this.weather = new WeatherResponseDTO(board.getWeather());
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.tags = board.getTags().stream().map(BoardTagDTO::new).collect(Collectors.toList()); // BoardTagDTO 생성자 사용
        this.image = board.getImage();
        this.views = board.getViews();
        this.commentCount = board.getCommentsSize();
        this.likeCount = board.getLikesSize();
        this.comments = board.getComments().stream().map(SimpleCommentDTO::new).collect(Collectors.toList()); //LocalDateTime에 대한 변환 작업
        this.registDate = board.getRegistDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    }
}
