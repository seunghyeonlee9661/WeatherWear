package com.sparta.WeatherWear.weather.repository;

import com.sparta.WeatherWear.weather.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
