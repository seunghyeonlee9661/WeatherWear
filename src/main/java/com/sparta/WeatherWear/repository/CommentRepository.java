package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
