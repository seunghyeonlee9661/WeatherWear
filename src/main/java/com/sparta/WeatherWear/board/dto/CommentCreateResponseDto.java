package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.Comment;
import com.sparta.WeatherWear.user.dto.UserSimpleDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentCreateResponseDto {
    private long id;
    // 사용자 정보
    private UserSimpleDto userSimpleDto = new UserSimpleDto();

    private Long BoardId;
    private String contents;
    private int commentCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    public CommentCreateResponseDto(Comment comment) {
        this.id = comment.getId();
        //
        this.userSimpleDto.setUserId(comment.getUser().getId());
        this.userSimpleDto.setNickname(comment.getUser().getNickname());
        this.userSimpleDto.setImage(comment.getUser().getImage());
        //
        this.BoardId = comment.getBoard().getId();
        this.contents = comment.getContents();
        this.commentCount = comment.getCommentLikes().size();
        //시간
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }

}
