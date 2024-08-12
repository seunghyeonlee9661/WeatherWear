package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
/* 사용자 좋아요 여부 저장 */
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 유저 정보

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board; // 게시물 정보

    public BoardLike(User user, Board board) {
        this.user = user;
        this.board = board;
    }
}
