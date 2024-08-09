package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(Long boardId);

    @Query("SELECT c FROM Comment c ORDER BY c.createdAt")
    List<Comment> findAllOrderedByCreatedAt();

    void deleteByBoardId(Long boardId);
}
