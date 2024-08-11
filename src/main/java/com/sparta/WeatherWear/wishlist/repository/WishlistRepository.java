package com.sparta.WeatherWear.wishlist.repository;

import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.wishlist.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId, Pageable pageable);
    List<Wishlist> findByUserId(Long userId);
    Optional<Wishlist> findByUserIdAndProductId(Long userId, Long productId);
    Boolean existsByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId AND (:type IS NULL OR w.type = :type) ORDER BY w.id DESC")
    Page<Wishlist> findByUserIdAndType(@Param("userId") Long userId, @Param("type") ClothesType type, Pageable pageable);
}
