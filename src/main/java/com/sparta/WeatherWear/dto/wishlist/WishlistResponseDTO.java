package com.sparta.WeatherWear.dto.wishlist;

import com.sparta.WeatherWear.entity.Clothes;
import com.sparta.WeatherWear.entity.NaverProduct;
import com.sparta.WeatherWear.entity.Wishlist;
import lombok.Getter;

@Getter
public class WishlistResponseDTO {
    private long id;
    private NaverProduct product;

    public WishlistResponseDTO(Wishlist wishlist) {
        this.id = wishlist.getId();
        this.product = wishlist.getProduct();
    }
}
