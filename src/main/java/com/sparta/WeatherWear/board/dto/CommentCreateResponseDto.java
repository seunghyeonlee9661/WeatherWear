package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentCreateResponseDto {
    private long id;
    private Long UserId;
    private Long BoardId;
    private String contents;
    private int commentCount;
    private LocalDateTime createTime;
    private LocalDateTime modifiedTime;

    public CommentCreateResponseDto(Comment comment) {
        this.id = comment.getId();
        this.UserId = comment.getUser().getId();
        this.BoardId = comment.getBoard().getId();
        this.contents = comment.getContents();
        this.commentCount = comment.getCommentLikes().size();
        this.createTime = comment.getCreatedAt();
        this.modifiedTime = comment.getModifiedAt();
    }

}
