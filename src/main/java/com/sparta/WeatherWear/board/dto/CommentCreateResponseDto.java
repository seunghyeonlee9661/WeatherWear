package com.sparta.WeatherWear.board.dto;

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
    private LocalDateTime registDate;
    private LocalDateTime updateDate;

    public CommentCreateResponseDto(Comment comment) {
        this.id = comment.getId();
        this.UserId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.image = comment.getUser().getImage();
        this.BoardId = comment.getBoard().getId();
        this.contents = comment.getContents();
        this.commentCount = comment.getCommentLikes().size();
        this.registDate = comment.getRegistDate();
        this.updateDate = comment.getUpdateDate();
    }

}
