package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
