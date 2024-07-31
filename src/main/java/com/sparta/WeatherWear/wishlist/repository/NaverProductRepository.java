package com.sparta.WeatherWear.wishlist.repository;

import com.sparta.WeatherWear.wishlist.entity.NaverProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaverProductRepository  extends JpaRepository<NaverProduct, Long> {
}
