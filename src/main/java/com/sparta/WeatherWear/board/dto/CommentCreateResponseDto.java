package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentCreateResponseDto {
    private long id;
    private Long UserId;
    private String nickname;
    private String image;
    private Long BoardId;
    private String contents;
    private int commentCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public CommentCreateResponseDto(Comment comment) {
        this.id = comment.getId();
        this.UserId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.image = comment.getUser().getImage();
        this.BoardId = comment.getBoard().getId();
        this.contents = comment.getContents();
        this.commentCount = comment.getCommentLikes().size();
        //시간
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

}
