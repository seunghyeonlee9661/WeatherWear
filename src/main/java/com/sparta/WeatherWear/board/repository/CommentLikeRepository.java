package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.Comment;
import com.sparta.WeatherWear.board.entity.CommentLike;
import com.sparta.WeatherWear.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    CommentLike findByUserAndComment(User user, Comment comment);
}
