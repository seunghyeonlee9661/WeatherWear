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



    // 페이지네이션: 게시물 커서를 기반으로 페이지 데이터를 불러오는 기능
    @Query("SELECT b FROM Board b " +
            "JOIN b.weather w " +
            "WHERE (:addressId IS NULL OR w.address.id = :addressId) " +
            "AND (:sky IS NULL OR w.SKY = :sky) " +
            "AND (:color IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.color = :color)) " +
            "AND (:type IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.type = :type)) " +
            "AND (:lastId IS NULL OR b.id < :lastId) " +
            "AND (b.isPrivate = false OR (b.isPrivate = true AND (:userId IS NOT NULL AND b.user.id = :userId))) " +
            "ORDER BY b.id DESC")
    List<Board> findBoardsAfterId(@Param("lastId") Long lastId,
                                  @Param("addressId") Long addressId,
                                  @Param("sky") Integer sky,
                                  @Param("color") ClothesColor color,
                                  @Param("type") ClothesType type,
                                  @Param("userId") Long userId,
                                  Pageable pageable);

    // 커서 값이 없을 경우: 최근 아이템 불러오는 기능
    @Query("SELECT b FROM Board b " +
            "JOIN b.weather w " +
            "WHERE (:addressId IS NULL OR w.address.id = :addressId) " +
            "AND (:sky IS NULL OR w.SKY = :sky) " +
            "AND (:color IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.color = :color)) " +
            "AND (:type IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.type = :type)) " +
            "AND (b.isPrivate = false OR (b.isPrivate = true AND (:userId IS NOT NULL AND b.user.id = :userId))) " +
            "ORDER BY b.id DESC")
    List<Board> findBoardsLatest(@Param("addressId") Long addressId,
                                 @Param("sky") Integer sky,
                                 @Param("color") ClothesColor color,
                                 @Param("type") ClothesType type,
                                 @Param("userId") Long userId,
                                 Pageable pageable);

    }
