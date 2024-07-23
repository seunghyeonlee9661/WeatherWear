package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
