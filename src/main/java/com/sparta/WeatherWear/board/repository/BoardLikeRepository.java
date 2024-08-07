package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/* 게시물 좋아요 Repository */
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    boolean existsByUserAndBoard(User user, Board board); // 사용자가 게시물을 좋아요를 했는지 확인하는 기능
    Optional<BoardLike> findByUserAndBoard(User user, Board board); // 좋아요 객체를 가져오는 기능
}
