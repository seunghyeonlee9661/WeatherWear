package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardImage;
import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.entity.Comment;
import com.sparta.WeatherWear.entity.Weather;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class BoardCreateResponseDto {

    private long id;
    private String userId;
    private String title;
    private String contents;
    private boolean isPrivate;
    private int stn;
    private LocalDateTime createTime;
    private LocalDateTime modifiedTime;
    private Weather weather;
    private List<BoardLike> boardLikes;
    private List<Comment> comments;
    private List<BoardTag> boardTags;
    private List<BoardImage> boardImages;

    public BoardCreateResponseDto(Board board) {
        this.id = board.getId();
        this.userId = board.getUserId();
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
        this.stn = board.getStn();
        this.createTime = board.getCreatedAt();
        this.modifiedTime = board.getModifiedAt();
        this.weather = board.getWeather();
        this.boardLikes = board.getBoardLikes();
        this.comments = board.getComments();
        this.boardTags = board.getBoardTags();
        this.boardImages = board.getBoardImages();
    }

}
