package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);

    void deleteByBoardId(Long boardId);
}
