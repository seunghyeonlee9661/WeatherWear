package com.sparta.WeatherWear.board.repository;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUserId(Long userId);

    // 게시물 추천용 검색 조건
    List<Board> findByUserAndWeather_SKYAndWeather_PTYAndWeather_TMPBetween(User user, int sky, int pty, Double minTmp, Double maxTmp);
    List<Board> findByWeather_SKYAndWeather_PTYAndWeather_TMPBetween(int sky, int pty, Double minTmp, Double maxTmp);

    @Query(value = "SELECT b.* " +
            "FROM board b " +
            "WHERE b.user_id = :userId " +
            "  AND b.weather_sky = :sky " +
            "  AND b.weather_pty = :pty " +
            "  AND b.weather_tmp BETWEEN :minTmp AND :maxTmp " +
            "ORDER BY b.likes_size DESC " +
            "LIMIT 5", nativeQuery = true)
    List<Board> findTopBoardsByUserAndWeather(@Param("userId") Long userId,
                                              @Param("sky") int sky,
                                              @Param("pty") int pty,
                                              @Param("minTmp") double minTmp,
                                              @Param("maxTmp") double maxTmp);

    @Query(value = "SELECT b.*, " +
            "       (b.likes_size * 5 + b.views * 0.5 + b.comments_size * 0.5) AS score " +
            "FROM board b " +
            "WHERE b.weather_sky = :sky " +
            "  AND b.weather_pty = :pty " +
            "  AND b.weather_tmp BETWEEN :minTmp AND :maxTmp " +
            "  AND b.user_id <> :userId " +
            "ORDER BY score DESC " +
            "LIMIT 9", nativeQuery = true)
    List<Board> findTopBoardsByWeatherExcludingUser(@Param("sky") int sky,
                                                    @Param("pty") int pty,
                                                    @Param("minTmp") double minTmp,
                                                    @Param("maxTmp") double maxTmp,
                                                    @Param("userId") Long userId);
}
