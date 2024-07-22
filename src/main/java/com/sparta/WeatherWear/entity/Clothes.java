package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.dto.clothes.ClothesRequestDTO;
import com.sparta.WeatherWear.enums.ClothesColor;
import com.sparta.WeatherWear.enums.ClothesType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
public class Clothes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private ClothesColor color;

    @Enumerated(EnumType.STRING)
    private ClothesType type;

    @Column(name = "image", length = 255, nullable = false)
    private String image;

    public Clothes(ClothesRequestDTO clothesRequestDTO, User user) {
        this.user = user;
        this.color = clothesRequestDTO.getColor();
        this.type = clothesRequestDTO.getType();
        this.image = clothesRequestDTO.getImage();
    }
}
