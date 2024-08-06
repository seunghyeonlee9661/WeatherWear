package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.weather.dto.WeatherResponseDTO;
import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class BoardListResponseDTO {
    private long id;
    private String title;
    private String address;
    private WeatherResponseDTO weather;
    private String image;
    private int views;
    private int commentCount;
    private int likeCount;
    private List<BoardTagDTO> tags;

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
