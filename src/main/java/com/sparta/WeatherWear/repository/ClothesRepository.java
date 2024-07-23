package com.sparta.WeatherWear.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sparta.WeatherWear.entity.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    Page<Clothes> findByUserId(Long userId, Pageable pageable);
}
