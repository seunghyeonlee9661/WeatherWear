package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.enums.ClothesColor;
import com.sparta.WeatherWear.enums.ClothesType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class BoardTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color", length = 50, nullable = false)
    private ClothesColor color;

    @Column(name = "type", length = 50, nullable = false)
    private ClothesType type;
}
