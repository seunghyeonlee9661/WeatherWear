package com.sparta.WeatherWear.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "url", length = 255, nullable = false)
    private String url;
}
