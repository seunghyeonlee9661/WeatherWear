package com.sparta.WeatherWear.dto.clothes;

import com.sparta.WeatherWear.enums.ClothesColor;
import com.sparta.WeatherWear.enums.ClothesType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/*
작성자 : 이승현
옷 데이터 요청 DTO
 */
@Getter
@NoArgsConstructor
public class ClothesRequestDTO {
    private ClothesColor color;
    private ClothesType type;
}