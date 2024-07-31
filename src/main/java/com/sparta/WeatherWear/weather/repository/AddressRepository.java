package com.sparta.WeatherWear.weather.repository;

import com.sparta.WeatherWear.weather.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(value = "SELECT * FROM address ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Address findRandomAddress();
}
