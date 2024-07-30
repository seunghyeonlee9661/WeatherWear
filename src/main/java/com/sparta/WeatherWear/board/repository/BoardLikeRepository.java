package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
}
