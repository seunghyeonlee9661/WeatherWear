package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.BoardImage;
import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.entity.Comment;
import com.sparta.WeatherWear.entity.Weather;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardUpdateRequestDto {

    private Long userId;
    private String title;
    private String contents;
    private boolean isPrivate;
    private int stn;
    private List<BoardLike> boardLikes;
    private List<Comment> comments;
    private List<BoardTag> boardTags;
}
