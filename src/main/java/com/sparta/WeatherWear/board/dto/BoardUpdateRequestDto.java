package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.board.entity.Comment;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.weather.entity.Weather;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardUpdateRequestDto {

    private long id;

    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String contents;

    private boolean isPrivate;

    private List<BoardTagDTO> tags;

    public BoardUpdateRequestDto(long id, String title, String contents, boolean isPrivate, List<BoardTagDTO> tags) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.isPrivate = isPrivate;
        this.tags = tags;
    }
}
