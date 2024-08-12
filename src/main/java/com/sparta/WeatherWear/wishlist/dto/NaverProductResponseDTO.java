package com.sparta.WeatherWear.wishlist.dto;

import com.sparta.WeatherWear.global.dto.ResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
작성자 : 이승현
네이버 아이템 요청 DTO
 */
@Getter
@NoArgsConstructor
public class NaverProductResponseDTO implements ResponseDTO {
    private Long productId;
    private String title;
    private String link;
    private String image;
    private long lprice;
    private long hprice;
    private String mallName;
    private String maker;
    private String brand;
    private String category1;
    private String category2;
    private String category3;
    private String category4;
    @Setter
    private String type;

    public NaverProductResponseDTO(WishlistRequestDTO productRequestDTO) {
        this.productId = productRequestDTO.getProductId();
        this.title = productRequestDTO.getTitle();
        this.link = productRequestDTO.getLink();
        this.image = productRequestDTO.getImage();
        this.lprice = productRequestDTO.getLprice();
        this.hprice = productRequestDTO.getHprice();
        this.mallName = productRequestDTO.getMallName();
        this.maker = productRequestDTO.getMaker();
        this.brand = productRequestDTO.getBrand();
        this.category1 = productRequestDTO.getCategory1();
        this.category2 = productRequestDTO.getCategory2();
        this.category3 = productRequestDTO.getCategory3();
        this.category4 = productRequestDTO.getCategory4();
    }

}
