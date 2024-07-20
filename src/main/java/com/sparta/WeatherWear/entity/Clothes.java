package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.enums.ClothesColor;
import com.sparta.WeatherWear.enums.ClothesType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "color", length = 50, nullable = false)
    private ClothesColor color;

    @Column(name = "type", length = 50, nullable = false)
    private ClothesType type;

    @Column(name = "image", length = 255, nullable = false)
    private String image;
}
