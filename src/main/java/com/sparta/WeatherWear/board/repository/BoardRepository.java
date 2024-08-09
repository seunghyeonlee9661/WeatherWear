package com.sparta.WeatherWear.board.repository;

import com.sparta.WeatherWear.board.entity.Board;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUserId(Long userId);

    // 페이지네이션 : 게시물 커서를 기반으로 페이지 데이터를 불러오는 기능
    @Query("SELECT b FROM Board b WHERE " +
            "(:search IS NULL OR b.title LIKE %:search%) AND " +
            "(:lastId IS NULL OR b.id < :lastId) AND " +
            "(b.isPrivate = false OR (b.isPrivate = true AND (:userId IS NOT NULL AND b.user.id = :userId))) " +
            "ORDER BY b.id DESC")
    List<Board> findBoardsAfterId(@Param("lastId") Long lastId,
                                  @Param("search") String search,
                                  @Param("userId") Long userId,
                                  Pageable pageable);

    // 커서 값이 없을 경우 : 최근 아이템 불러오는 기능
    @Query("SELECT b FROM Board b WHERE " +
            "(:search IS NULL OR b.title LIKE %:search%) AND " +
            "(b.isPrivate = false OR (b.isPrivate = true AND (:userId IS NOT NULL AND b.user.id = :userId))) " +
            "ORDER BY b.id DESC")
    List<Board> findBoardsLatest(@Param("search") String search,
                                 @Param("userId") Long userId,
                                 Pageable pageable);


    @Query("SELECT b FROM Board b ORDER BY b.createdAt DESC")
    Page<Board> findAllOrderedByCreatedAt(Pageable pageable);

    List<Board> findAllByAddress(String city);
}
