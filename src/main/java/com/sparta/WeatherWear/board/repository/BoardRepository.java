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

    @Query("SELECT b FROM Board b ORDER BY b.createdAt DESC")
    Page<Board> findAllOrderedByCreatedAt(Pageable pageable);


    // 커서 기반 페이지네이션: lastId 이후의 게시물을 불러옵니다.
    @Query("SELECT b FROM Board b " +
            "WHERE (:color IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.color = :color)) " +
            "AND (:type IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.type = :type)) " +
            "AND (:lastId IS NULL OR b.id < :lastId) " +
            "AND b.isPrivate = false " +  // isPrivate가 true인 경우 제외
            "AND (:keyword IS NULL OR b.title LIKE CONCAT('%', :keyword, '%') OR b.content LIKE CONCAT('%', :keyword, '%') OR b.address LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY b.id DESC")
    List<Board> findBoardsAfterId(@Param("lastId") Long lastId,
                                  @Param("color") ClothesColor color,
                                  @Param("type") ClothesType type,
                                  Pageable pageable,
                                  @Param("keyword") String keyword);



    // 최신 게시물 조회: 커서 값이 없을 경우
    @Query("SELECT b FROM Board b " +
            "WHERE (:color IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.color = :color)) " +
            "AND (:type IS NULL OR EXISTS (SELECT 1 FROM b.boardTags t WHERE t.type = :type)) " +
            "AND b.isPrivate = false " +  // 사용자 아이디 확인 조건 제거
            "AND (:keyword IS NULL OR b.title LIKE CONCAT('%', :keyword, '%') OR b.content LIKE CONCAT('%', :keyword, '%') OR b.address LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY b.id DESC")
    List<Board> findBoardsLatest(@Param("color") ClothesColor color,
                                 @Param("type") ClothesType type,
                                 Pageable pageable,
                                 @Param("keyword") String keyword);

    // 사용자 게시물 검색 필터링
    @Query("SELECT b FROM Board b " +
            "JOIN b.weather w " +
            "WHERE b.user.id = :userId " +
            "AND ( " +
            "  (:pty IS NULL AND :sky IS NULL) " +
            "  OR (:pty = 1 AND (w.PTY = 1 OR w.PTY = 2 OR w.PTY = 4)) " +
            "  OR (:pty = 3 AND w.PTY = 3) " +
            "  OR (:pty IS NULL AND (:sky IS NOT NULL AND w.SKY = :sky AND w.PTY = 0)) " +
            ") " +
            "AND (:keyword IS NULL OR b.title LIKE CONCAT('%', :keyword, '%') OR b.content LIKE CONCAT('%', :keyword, '%')  OR b.address LIKE CONCAT('%', :keyword, '%')) " +
            "ORDER BY b.id DESC")
    Page<Board> findByUserId(@Param("userId") Long userId,
                             @Param("pty") Integer pty,
                             @Param("sky") Integer sky,
                             @Param("keyword") String keyword,
                             Pageable pageable);

    // 추천 아이템 선정을 위해 점수를 선정하고 상위 5개를 선정하는 기능
    @Query(value = "SELECT b.*, " +
            "       (COALESCE(b.like_count, 0) * 5 + COALESCE(b.views, 0) * 1 + COALESCE(b.comment_count, 0) * 0.5) AS score " +
            "FROM ( " +
            "    SELECT b.id, b.user_id, b.weather_id, b.address, b.title, b.content, b.is_private, b.image, " +
            "           b.created_at, b.updated_at, b.views, " +
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
            "LIMIT 4", nativeQuery = true)
    List<Board> findTopBoardsByUserAndWeatherWithScore(@Param("userId") Long userId,
                                                       @Param("sky") int sky,
                                                       @Param("pty") int pty,
                                                       @Param("minTmp") double minTmp,
                                                       @Param("maxTmp") double maxTmp);

    // 추천 아이템 선정을 위해 점수를 선정하고 상위 9개를 선정하는 기능
    @Query(value = "SELECT b.*, " +
            "       (COALESCE(b.like_count, 0) * 5 + COALESCE(b.views, 0) * 1 + COALESCE(b.comment_count, 0) * 0.5) AS score " +
            "FROM ( " +
            "    SELECT b.id, b.user_id, b.weather_id, b.address, b.title, b.content, b.is_private, b.image, " +
            "           b.created_at, b.updated_at, b.views, " +
            "           COUNT(DISTINCT bl.id) AS like_count, " +
            "           COUNT(DISTINCT c.id) AS comment_count " +
            "    FROM board b " +
            "    LEFT JOIN board_like bl ON b.id = bl.board_id " +
            "    LEFT JOIN comment c ON b.id = c.board_id " +
            "    JOIN weather w ON b.weather_id = w.id " +
            "    WHERE b.user_id <> :userId " +  // 수정된 부분
            "      AND w.SKY = :sky " +
            "      AND w.PTY = :pty " +
            "      AND w.TMP BETWEEN :minTmp AND :maxTmp " +
            "    GROUP BY b.id " +
            ") AS b " +
            "ORDER BY score DESC " +
            "LIMIT 4", nativeQuery = true)
    List<Board> findTopBoardsByWeatherExcludingUserWithScore(@Param("sky") int sky,
                                                             @Param("pty") int pty,
                                                             @Param("minTmp") double minTmp,
                                                             @Param("maxTmp") double maxTmp,
                                                             @Param("userId") Long userId);

    // 추천 아이템 선정을 위해 점수를 선정하고 상위 9개를 선정하는 기능 (비로그인 사용자)
    @Query(value = "SELECT b.*, " +
            "       (COALESCE(b.like_count, 0) * 5 + COALESCE(b.views, 0) * 1 + COALESCE(b.comment_count, 0) * 0.5) AS score " +
            "FROM ( " +
            "    SELECT b.id, b.user_id, b.weather_id, b.address, b.title, b.content, b.is_private, b.image, " +
            "           b.created_at, b.updated_at, b.views, " +
            "           COUNT(DISTINCT bl.id) AS like_count, " +
            "           COUNT(DISTINCT c.id) AS comment_count " +
            "    FROM board b " +
            "    LEFT JOIN board_like bl ON b.id = bl.board_id " +
            "    LEFT JOIN comment c ON b.id = c.board_id " +
            "    JOIN weather w ON b.weather_id = w.id " +
            "      AND w.SKY = :sky " +
            "      AND w.PTY = :pty " +
            "      AND w.TMP BETWEEN :minTmp AND :maxTmp " +
            "    GROUP BY b.id " +
            ") AS b " +
            "ORDER BY score DESC " +
            "LIMIT 4", nativeQuery = true)
    List<Board> findTopBoardsByWeatherWithScore(@Param("sky") int sky,
                                                 @Param("pty") int pty,
                                                 @Param("minTmp") double minTmp,
                                                 @Param("maxTmp") double maxTmp);


}