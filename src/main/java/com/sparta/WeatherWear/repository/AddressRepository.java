package com.sparta.WeatherWear.repository;

import com.sparta.WeatherWear.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
