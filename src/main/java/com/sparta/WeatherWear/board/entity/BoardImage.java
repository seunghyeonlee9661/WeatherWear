package com.sparta.WeatherWear.board.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "save_folder", length = 255, nullable = false)
    private String saveFolder;

    @Column(name = "original_file", length = 255, nullable = false)
    private String originalFile;

    @Column(name = "save_file", length = 255, nullable = false)
    private String saveFile;
}
