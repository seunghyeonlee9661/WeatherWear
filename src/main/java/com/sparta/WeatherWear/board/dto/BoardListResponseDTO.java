package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.weather.dto.WeatherResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/* 게시물 목록에 대한 리스트 아이템 반환 DTO */
@Getter
@Setter
public class BoardListResponseDTO {
    private long id;
    private String title; // 제목
    private String address; // 주소
    private WeatherResponseDTO weather; // 날씨 정보
    private String image; // 이미지
    private int views; // 조회수
    private int commentCount; // 댓글수
    private int likeCount; // 좋아요 수
    private List<BoardTagDTO> tags; // 태그 목록

    public BoardListResponseDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.address = board.getAddress();
        this.weather = new WeatherResponseDTO(board.getWeather());
        this.image = board.getImage();
        this.views = board.getViews();
        this.commentCount = board.getCommentsSize();
        this.likeCount = board.getLikesSize();
        this.tags = board.getTags().stream().map(BoardTagDTO::new).collect(Collectors.toList()); // BoardTagDTO 생성자 사용
    }
}
