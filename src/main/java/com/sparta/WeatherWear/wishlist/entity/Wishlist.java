package com.sparta.WeatherWear.wishlist.entity;

import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
/*
작성자 : 이승현
위시리스트 Entity
 */
@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private NaverProduct product;

    @Enumerated(EnumType.STRING)
    private ClothesType type;

    public Wishlist(User user, NaverProduct product,ClothesType type){
        this.user = user;
        this.product = product;
        this.type = type;
    }
}
