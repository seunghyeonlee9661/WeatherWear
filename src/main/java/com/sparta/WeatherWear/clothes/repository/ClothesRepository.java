package com.sparta.WeatherWear.clothes.repository;

import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sparta.WeatherWear.clothes.entity.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    List<Clothes> findByUserId(Long userId);
    List<Clothes> findByUserAndTypeIn(User user, List<ClothesType> types);
}
