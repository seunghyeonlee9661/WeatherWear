package com.sparta.WeatherWear.repository;


import com.sparta.WeatherWear.entity.Address;
import com.sparta.WeatherWear.entity.Weather;
import com.sparta.WeatherWear.entity.WeatherNew;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public interface WeatherNewRepository extends JpaRepository<WeatherNew, Long> {
    Optional<WeatherNew> findByAddressAndDate(Address address, Date date);
}
