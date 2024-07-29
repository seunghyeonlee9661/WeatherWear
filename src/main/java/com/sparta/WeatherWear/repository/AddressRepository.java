package com.sparta.WeatherWear.repository;

import com.sparta.WeatherWear.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(value = "SELECT * FROM address ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Address findRandomAddress();
}
