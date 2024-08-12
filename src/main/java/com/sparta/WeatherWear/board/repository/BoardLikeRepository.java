package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Optional<BoardLike> findByUserAndBoard(User user, Board board);
    boolean existsByUserAndBoard(User user, Board board); // 사용자가 게시물을 좋아요를 했는지 확인하는 기능

    @Query("SELECT COUNT(bl) FROM BoardLike bl WHERE bl.board.id = :boardId")
    Integer countLikesByBoardId(@Param("boardId") Long boardId);
}
