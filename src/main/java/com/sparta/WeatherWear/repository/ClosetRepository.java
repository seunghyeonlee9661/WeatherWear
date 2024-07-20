package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClosetRepository extends JpaRepository<Clothes, Integer> {
}
