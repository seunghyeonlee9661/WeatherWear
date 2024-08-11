package com.sparta.WeatherWear.clothes.repository;

import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sparta.WeatherWear.clothes.entity.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    List<Clothes> findByUserAndTypeIn(User user, List<ClothesType> types);

    @Query("SELECT c FROM Clothes c WHERE c.user.id = :userId AND (:type IS NULL OR :type = '' OR c.type = :type) AND (:color IS NULL OR :color = '' OR c.color = :color) ORDER BY c.id DESC")
    Page<Clothes> findByUserIdAndFilters(@Param("userId") Long userId, @Param("type") ClothesType type, @Param("color") ClothesColor color,Pageable pageable);
}
