package com.sparta.WeatherWear.clothes.entity;

import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.user.entity.User;
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

    public Clothes(ClothesColor color,ClothesType type, User user,String image) {
        this.user = user;
        this.color = color;
        this.type = type;
        this.image = image;
    }

    public void update (ClothesColor color,ClothesType type,String image){
        this.image = image;
        this.color = color;
        this.type = type;
    }
}
