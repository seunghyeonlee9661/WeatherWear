package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.user.enums.UserGender;
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

    // 색상
    @Enumerated(EnumType.STRING)
    private ClothesColor color;

    // 종류
    @Enumerated(EnumType.STRING)
    private ClothesType type;

    public BoardTag(Board board, ClothesColor color, ClothesType type) {
        this.board = board;
        this.color = color;
        this.type = type;
    }

}
