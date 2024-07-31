package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
