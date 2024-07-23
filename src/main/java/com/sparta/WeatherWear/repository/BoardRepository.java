package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Board;
import com.sparta.WeatherWear.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
