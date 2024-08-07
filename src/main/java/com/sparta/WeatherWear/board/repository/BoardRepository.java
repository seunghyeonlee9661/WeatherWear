package com.sparta.WeatherWear.board.repository;

import com.sparta.WeatherWear.board.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUserId(Long userId); // 사용자의 게시물을 불러오는 기능

    // 페이지네이션 : 게시물 커서를 기반으로 페이지 데이터를 불러오는 기능
    @Query("SELECT b FROM Board b WHERE (:search IS NULL OR b.title LIKE %:search%) AND (:lastId IS NULL OR b.id < :lastId) ORDER BY b.id DESC")
    List<Board> findBoardsAfterId(@Param("lastId") Long lastId, @Param("search") String search, Pageable pageable);
    
    // 커서 값이 없을 경우 : 최근 아이템 불러오는 기능
    @Query("SELECT b FROM Board b WHERE (:search IS NULL OR b.title LIKE %:search%) ORDER BY b.id DESC")
    List<Board> findBoardsLatest(@Param("search") String search, Pageable pageable);

    // 추천 아이템 선정을 위해 점수를 선정하고 상위 5개를 선정하는 기능 : 미완성...
    @Query(value = "SELECT b.* " +
            "FROM board b " +
            "JOIN weather w ON b.weather_id = w.id " +
            "WHERE b.user_id = :userId " +
            "  AND w.sky = :sky " +
            "  AND w.pty = :pty " +
            "  AND w.tmp BETWEEN :minTmp AND :maxTmp " +
            "ORDER BY b.likes_size DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Board> findTopBoardsByUserAndWeather(@Param("userId") Long userId,
                                              @Param("sky") int sky,
                                              @Param("pty") int pty,
                                              @Param("minTmp") double minTmp,
                                              @Param("maxTmp") double maxTmp);

    // 추천 아이템 선정을 위해 점수를 선정하고 상위 5개를 선정하는 기능 : 미완성...
    @Query(value = "SELECT b.*, " +
            "       (b.likes_size * 5 + b.views * 0.5 + b.comments_size * 0.5) AS score " +
            "FROM board b " +
            "JOIN weather w ON b.weather_id = w.id " +
            "WHERE w.sky = :sky " +
            "  AND w.pty = :pty " +
            "  AND w.tmp BETWEEN :minTmp AND :maxTmp " +
            "  AND b.user_id <> :userId " +
            "ORDER BY score DESC " +
            "LIMIT 9", nativeQuery = true)
    List<Board> findTopBoardsByWeatherExcludingUser(@Param("sky") int sky,
                                                    @Param("pty") int pty,
                                                    @Param("minTmp") double minTmp,
                                                    @Param("maxTmp") double maxTmp,
                                                    @Param("userId") Long userId);

}
