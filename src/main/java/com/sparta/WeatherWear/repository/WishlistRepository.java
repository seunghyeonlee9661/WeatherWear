package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Clothes;
import com.sparta.WeatherWear.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Page<Wishlist> findByUserId(Long userId, Pageable pageable);
}
