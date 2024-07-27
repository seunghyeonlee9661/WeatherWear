package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Board;
import com.sparta.WeatherWear.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUserAndWeather_SKYAndWeather_PTYAndWeather_TMPBetween(User user, int sky, int pty, Double minTmp, Double maxTmp);
    List<Board> findByWeather_SKYAndWeather_PTYAndWeather_TMPBetween(int sky, int pty, Double minTmp, Double maxTmp);
}
