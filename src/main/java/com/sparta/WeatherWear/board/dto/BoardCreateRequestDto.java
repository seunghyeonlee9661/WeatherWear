package com.sparta.WeatherWear.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/*
 * 작성자 : 이승현
 * 게시물 CRUD와 좋아요 기능 구현
 * */
@Getter
@Setter
public class BoardCreateRequestDto {

    // 주소 텍스트
    @NotBlank(message = "주소값이 없습니다.")
    private String address;

    // 법정동 코드
    @NotNull(message = "행정동 코드값이 없습니다.")
    private Long address_id;

    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String contents;

    private boolean isPrivate;

    private List<BoardTagDTO> tags;

    public BoardCreateRequestDto(String address, Long address_id, String title, String contents, boolean isPrivate, List<BoardTagDTO> tags) {
        this.address = address;
        this.address_id = address_id;
        this.title = title;
        this.contents = contents;
        this.isPrivate = isPrivate;
        this.tags = tags;
    }
}
