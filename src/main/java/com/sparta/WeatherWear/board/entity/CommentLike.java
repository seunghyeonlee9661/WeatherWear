package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 유저 정보

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment; // 댓글 정보

    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}
