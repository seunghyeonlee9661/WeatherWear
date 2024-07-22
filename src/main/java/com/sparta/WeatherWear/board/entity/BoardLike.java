package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
}
