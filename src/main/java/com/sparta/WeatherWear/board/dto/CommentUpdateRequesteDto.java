package com.sparta.WeatherWear.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 추가
public class CommentUpdateRequesteDto {

    private String contents;
}
