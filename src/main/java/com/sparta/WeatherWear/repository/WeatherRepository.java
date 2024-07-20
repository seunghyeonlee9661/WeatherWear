package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Integer> {
}
