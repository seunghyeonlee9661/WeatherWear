package com.sparta.WeatherWear.board.repository;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUserId(Long userId);

//    // 게시물 추천용 검색 조건
//    List<Board> findByUserAndWeather_SKYAndWeather_PTYAndWeather_TMPBetween(User user, int sky, int pty, Double minTmp, Double maxTmp);
//    List<Board> findByWeather_SKYAndWeather_PTYAndWeather_TMPBetween(int sky, int pty, Double minTmp, Double maxTmp);

    @Query("SELECT b FROM Board b ORDER BY b.createdAt DESC")
    Page<Board> findAllOrderedByCreatedAt(Pageable pageable);


    // 커서 기반 페이지네이션: lastId 이후의 게시물을 불러옵니다.
    @Query("SELECT b FROM Board b " +
            "JOIN b.weather w " +
            "WHERE (:address IS NULL OR w.address.name LIKE CONCAT(:address, '%')) " +
            "AND (:color IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.color = :color)) " +
            "AND (:type IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.type = :type)) " +
            "AND (:lastId IS NULL OR b.id < :lastId) " +
            "AND (b.isPrivate = false OR (b.isPrivate = true AND (:userId IS NOT NULL AND b.user.id = :userId))) " +
            "AND (:keyword IS NULL OR b.title LIKE CONCAT('%', :keyword, '%') OR b.contents LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY b.id DESC")
    List<Board> findBoardsAfterId(@Param("lastId") Long lastId,
                                  @Param("address") String address,
                                  @Param("color") ClothesColor color,
                                  @Param("type") ClothesType type,
                                  @Param("userId") Long userId,
                                  Pageable pageable,
                                  @Param("keyword") String keyword);

    // 최신 게시물 조회: 커서 값이 없을 경우
    @Query("SELECT b FROM Board b " +
            "JOIN b.weather w " +
            "WHERE (:address IS NULL OR w.address.name LIKE CONCAT(:address, '%')) " +
            "AND (:color IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.color = :color)) " +
            "AND (:type IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.type = :type)) " +
            "AND (b.isPrivate = false OR (b.isPrivate = true AND (:userId IS NOT NULL AND b.user.id = :userId))) " +
            "AND (:keyword IS NULL OR b.title LIKE CONCAT('%', :keyword, '%') OR b.contents LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY b.id DESC")
    List<Board> findBoardsLatest(@Param("address") String address,
                                 @Param("color") ClothesColor color,
                                 @Param("type") ClothesType type,
                                 @Param("userId") Long userId,
                                 Pageable pageable,
                                 @Param("keyword") String keyword);
}