package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Optional<Weather> findByStnAndDate(long stn, LocalDateTime date);
}
