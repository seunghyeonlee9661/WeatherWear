package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.Comment;
import com.sparta.WeatherWear.user.dto.SimpleUserDTO;
import lombok.NoArgsConstructor;

public class SimpleCommentDTO {
    private Long id;
    private SimpleUserDTO user;
    private String contents;

    public SimpleCommentDTO(Comment comment){
        this.id = comment.getId();
        this.user = new SimpleUserDTO(comment.getUser());
        this.contents = comment.getContents();
    }
}
