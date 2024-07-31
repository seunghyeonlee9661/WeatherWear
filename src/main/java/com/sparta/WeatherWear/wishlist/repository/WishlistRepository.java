package com.sparta.WeatherWear.wishlist.repository;

import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.wishlist.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Page<Wishlist> findByUserId(Long userId, Pageable pageable);
    List<Wishlist> findByUserId(Long userId);
}
