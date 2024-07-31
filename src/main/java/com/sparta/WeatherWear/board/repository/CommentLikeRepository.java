package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
