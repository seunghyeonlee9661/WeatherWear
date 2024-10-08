package com.sparta.WeatherWear.wishlist.dto;

import com.sparta.WeatherWear.clothes.enums.ClothesType;
import lombok.Getter;
/*
작성자 : 이승현
네이버 아이템 요청 DTO
 */
@Getter
public class WishlistRequestDTO {
    private Long productId;
    private String title;
    private String link;
    private String image;
    private long lprice;
    private long hprice;
    private int productType;
    private String mallName;
    private String maker;
    private String brand;
    private String category1;
    private String category2;
    private String category3;
    private String category4;
    private ClothesType type;
}
