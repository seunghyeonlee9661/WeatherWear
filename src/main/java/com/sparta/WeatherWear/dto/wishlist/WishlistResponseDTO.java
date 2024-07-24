package com.sparta.WeatherWear.dto.wishlist;

import com.sparta.WeatherWear.entity.NaverProduct;
import com.sparta.WeatherWear.entity.Wishlist;
import lombok.Getter;
/*
작성자 : 이승현
위시리스트 반환 요청 DTO
 */
@Getter
public class WishlistResponseDTO {
    private long id;
    private NaverProduct product;

    public WishlistResponseDTO(Wishlist wishlist) {
        this.id = wishlist.getId();
        this.product = wishlist.getProduct();
    }
}
