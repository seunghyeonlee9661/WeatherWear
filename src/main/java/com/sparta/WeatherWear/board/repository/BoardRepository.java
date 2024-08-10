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
            "WHERE (:address IS NULL OR b.address LIKE CONCAT(:address, '%')) " +
            "AND (:color IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.color = :color)) " +
            "AND (:type IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.type = :type)) " +
            "AND (:lastId IS NULL OR b.id < :lastId) " +
            "AND (b.isPrivate = false OR (b.isPrivate = true AND (:userId IS NOT NULL AND b.user.id = :userId))) " +
            "AND (:keyword IS NULL OR b.title LIKE CONCAT('%', :keyword, '%') OR b.content LIKE CONCAT('%', :keyword, '%')) " +
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
            "WHERE (:address IS NULL OR b.address LIKE CONCAT(:address, '%')) " +
            "AND (:color IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.color = :color)) " +
            "AND (:type IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.type = :type)) " +
            "AND (b.isPrivate = false OR (b.isPrivate = true AND (:userId IS NOT NULL AND b.user.id = :userId))) " +
            "AND (:keyword IS NULL OR b.title LIKE CONCAT('%', :keyword, '%') OR b.content LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY b.id DESC")
    List<Board> findBoardsLatest(@Param("address") String address,
                                 @Param("color") ClothesColor color,
                                 @Param("type") ClothesType type,
                                 @Param("userId") Long userId,
                                 Pageable pageable,
                                 @Param("keyword") String keyword);

    // 사용자 게시물 검색 필터링
    @Query("SELECT b FROM Board b " +
            "JOIN b.weather w " +
            "WHERE b.user.id = :userId " +
            "AND (:pty IS NULL OR :sky IS NULL OR w.PTY = :pty OR w.SKY = :sky) " +
            "AND (:keyword IS NULL OR b.title LIKE CONCAT('%', :keyword, '%') OR b.content LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY b.id DESC")
    Page<Board> findByUserId(@Param("userId") Long userId,
                             @Param("pty") Integer pty,
                             @Param("sky") Integer sky,
                             @Param("keyword") String keyword,
                             Pageable pageable);

    // 추천 아이템 선정을 위해 점수를 선정하고 상위 5개를 선정하는 기능
    @Query(value = "SELECT b.*, " +
            "       (COALESCE(like_count, 0) * 5 + COALESCE(view_count, 0) * 1 + COALESCE(comment_count, 0) * 0.5) AS score " +
            "FROM ( " +
            "    SELECT b.id, b.user_id, b.weather_id, b.address, b.title, b.content, b.is_private, b.image, b.views AS view_count, " +
            "           COUNT(DISTINCT bl.id) AS like_count, " +
            "           COUNT(DISTINCT c.id) AS comment_count " +
            "    FROM board b " +
            "    LEFT JOIN board_like bl ON b.id = bl.board_id " +
            "    LEFT JOIN comment c ON b.id = c.board_id " +
            "    JOIN weather w ON b.weather_id = w.id " +
            "    WHERE b.user_id = :userId " +
            "      AND w.SKY = :sky " +
            "      AND w.PTY = :pty " +
            "      AND w.TMP BETWEEN :minTmp AND :maxTmp " +
            "    GROUP BY b.id " +
            ") AS b " +
            "ORDER BY score DESC " +
            "LIMIT 9", nativeQuery = true)
    List<Board> findTopBoardsByUserAndWeatherWithScore(@Param("userId") Long userId,
                                                       @Param("sky") int sky,
                                                       @Param("pty") int pty,
                                                       @Param("minTmp") double minTmp,
                                                       @Param("maxTmp") double maxTmp);

    // 추천 아이템 선정을 위해 점수를 선정하고 상위 9개를 선정하는 기능
    @Query(value = "SELECT b.*, " +
            "       (COALESCE(like_count, 0) * 5 + COALESCE(view_count, 0) * 1 + COALESCE(comment_count, 0) * 0.5) AS score " +
            "FROM ( " +
            "    SELECT b.id, b.user_id, b.weather_id, b.address, b.title, b.content, b.is_private, b.image, b.views AS view_count, " +
            "           COUNT(DISTINCT bl.id) AS like_count, " +
            "           COUNT(DISTINCT c.id) AS comment_count " +
            "    FROM board b " +
            "    LEFT JOIN board_like bl ON b.id = bl.board_id " +
            "    LEFT JOIN comment c ON b.id = c.board_id " +
            "    JOIN weather w ON b.weather_id = w.id " +
            "    WHERE w.SKY = :sky " +
            "      AND w.PTY = :pty " +
            "      AND w.TMP BETWEEN :minTmp AND :maxTmp " +
            "      AND b.user_id <> :userId " +
            "    GROUP BY b.id " +
            ") AS b " +
            "ORDER BY score DESC " +
            "LIMIT 9", nativeQuery = true)
    List<Board> findTopBoardsByWeatherExcludingUserWithScore(@Param("sky") int sky,
                                                             @Param("pty") int pty,
                                                             @Param("minTmp") double minTmp,
                                                             @Param("maxTmp") double maxTmp,
                                                             @Param("userId") Long userId);
}