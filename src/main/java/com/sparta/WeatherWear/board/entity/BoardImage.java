package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.time.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor // 디폴트 생성자
public class BoardImage{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_path", length = 255, nullable = false)
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board; // FK

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    public BoardImage(Board board, String imagePath) {
        this.board = board;
        this.imagePath = imagePath;
        this.createdAt = LocalDateTime.now();
    }

}
