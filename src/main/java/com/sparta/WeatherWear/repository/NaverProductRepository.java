package com.sparta.WeatherWear.repository;

import com.sparta.WeatherWear.entity.NaverProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaverProductRepository  extends JpaRepository<NaverProduct, Long> {
}
