package com.sparta.WeatherWear.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "naver_item", nullable = false)
    private String naverItem;
}
