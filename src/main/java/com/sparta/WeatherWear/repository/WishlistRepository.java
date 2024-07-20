package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
}
