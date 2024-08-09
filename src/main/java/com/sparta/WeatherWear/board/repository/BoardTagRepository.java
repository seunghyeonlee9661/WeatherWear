package com.sparta.WeatherWear.board.repository;


import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTagRepository extends JpaRepository<BoardTag, Long> {
    void deleteByBoard(Board board); // 게시물의 모든 태그 삭제 기능 : 게시물 수정시 태그를 모두 삭제하고 다시 저장하기 위해
}
