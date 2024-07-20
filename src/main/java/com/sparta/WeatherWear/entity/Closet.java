package com.sparta.WeatherWear.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Closet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "color", length = 50, nullable = false)
    private String color;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "image", length = 255, nullable = false)
    private String image;
}
