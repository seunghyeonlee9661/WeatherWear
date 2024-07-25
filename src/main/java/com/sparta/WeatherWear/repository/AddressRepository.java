package com.sparta.WeatherWear.repository;

import com.sparta.WeatherWear.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    // 특정 격자 좌표와 주소를 기반으로 행정구역 정보를 찾기 위한 메서드
    Optional<Address> findByCityAndCountyAndDistrict(String city, String county, String district);
}
