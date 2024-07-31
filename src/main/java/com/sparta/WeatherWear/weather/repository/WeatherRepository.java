package com.sparta.WeatherWear.weather.repository;


import com.sparta.WeatherWear.weather.entity.Address;
import com.sparta.WeatherWear.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Optional<Weather> findByAddressAndDate(Address address, Date date);
}
