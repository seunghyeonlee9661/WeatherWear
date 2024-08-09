package com.sparta.WeatherWear.user.repository;


import com.sparta.WeatherWear.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByKakaoId(Long kakaoId);
    Boolean existsByNickname(String nickname);
    Boolean existsByEmail(String email);
}
