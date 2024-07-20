package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Board;
import com.sparta.WeatherWear.entity.BoardTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTagRepository extends JpaRepository<BoardTag, Integer> {
}
