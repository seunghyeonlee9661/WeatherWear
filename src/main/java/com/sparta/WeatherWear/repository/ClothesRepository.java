package com.sparta.WeatherWear.repository;

import com.sparta.WeatherWear.entity.User;
import com.sparta.WeatherWear.enums.ClothesType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sparta.WeatherWear.entity.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    Page<Clothes> findByUserId(Long userId, Pageable pageable);
    List<Clothes> findByUserAndTypeIn(User user, List<ClothesType> types);
}
