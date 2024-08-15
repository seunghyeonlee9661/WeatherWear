package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.board.time.Timestamped;
import com.sparta.WeatherWear.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "contents", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String contents; // 내용물

    public Comment(User user, Board board, String contents) {
        this.user = user;
        this.board = board;
        this.contents = contents;
    }
    public Comment update(String contents) {
        this.contents = contents;
        return this;
    }

}
