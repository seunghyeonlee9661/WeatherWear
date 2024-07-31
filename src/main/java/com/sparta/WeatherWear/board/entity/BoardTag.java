package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.enums.ClothesColor;
import com.sparta.WeatherWear.enums.ClothesType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
public class BoardTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "color", length = 50, nullable = false)
    private ClothesColor color;

    @Column(name = "type", length = 50, nullable = false)
    private ClothesType type;

    public BoardTag(Board board, ClothesColor color, ClothesType type) {
        this.board = board;
        this.color = color;
        this.type = type;
    }
}
