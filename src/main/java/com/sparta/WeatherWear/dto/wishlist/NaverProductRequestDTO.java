package com.sparta.WeatherWear.dto.wishlist;

import lombok.Getter;
/*
작성자 : 이승현
네이버 아이템 요청 DTO
 */
@Getter
public class NaverProductRequestDTO {
    private Long productId;
    private String title;
    private String link;
    private String image;
    private long lprice;
    private long hprice;
    private String mallName;
    private int productType;
    private String maker;
    private String brand;
    private String category1;
    private String category2;
    private String category3;
    private String category4;
}
