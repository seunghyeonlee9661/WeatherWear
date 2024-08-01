package com.sparta.WeatherWear.wishlist.repository;

import com.sparta.WeatherWear.wishlist.entity.Wishlist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId, Pageable pageable);
    List<Wishlist> findByUserId(Long userId);
    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);
    Boolean existsByUserIdAndProductId(Long userId, Long productId);
}
