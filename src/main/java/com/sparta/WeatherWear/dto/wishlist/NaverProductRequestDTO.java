package com.sparta.WeatherWear.dto.wishlist;

import lombok.Getter;

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
}
