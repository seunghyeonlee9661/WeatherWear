package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
}
