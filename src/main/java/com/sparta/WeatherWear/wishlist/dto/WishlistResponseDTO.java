package com.sparta.WeatherWear.wishlist.dto;

import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.wishlist.entity.NaverProduct;
import com.sparta.WeatherWear.wishlist.entity.Wishlist;
import lombok.Getter;
/*
작성자 : 이승현
위시리스트 반환 요청 DTO
 */
@Getter
public class WishlistResponseDTO {
    private long id;
    private NaverProduct product;
    private ClothesType type;

    public WishlistResponseDTO(Wishlist wishlist) {
        this.id = wishlist.getId();
        this.product = wishlist.getProduct();
        this.type = wishlist.getType();
    }
}
