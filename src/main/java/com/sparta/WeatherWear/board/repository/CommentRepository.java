package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId ORDER BY c.createdAt ASC")
    List<Comment> findAllByBoardIdOrderedByCreatedAt(Long boardId);
}
