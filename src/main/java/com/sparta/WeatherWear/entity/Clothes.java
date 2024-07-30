package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.dto.clothes.ClothesRequestDTO;
import com.sparta.WeatherWear.enums.ClothesColor;
import com.sparta.WeatherWear.enums.ClothesType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
/*
작성자 : 이승현
 사용자의 옷 목록 Entity
 */
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

    @Column(name = "image", nullable = true)
    private String image;

    public Clothes(ClothesRequestDTO clothesRequestDTO, User user) {
        this.user = user;
        this.color = clothesRequestDTO.getColor();
        this.type = clothesRequestDTO.getType();
    }

    public void updateImage (String image){
        this.image = image;
    }
}
