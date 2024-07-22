package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Board;
import com.sparta.WeatherWear.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
}
