package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.dto.wishlist.NaverProductRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
/*
작성자 : 이승현
 네이버 제품 정보 Entity - 위시리스트의 목록에서 해당 아이템을 참조합니다.
 */
@Getter
@Entity
@NoArgsConstructor
public class NaverProduct {
    @Id
    private Long id;

    @Column(name = "title", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String title;

    @Column(name = "link", columnDefinition = "MEDIUMTEXT")
    private String link;

    @Column(name = "image", columnDefinition = "MEDIUMTEXT")
    private String image;

    @Column(name = "lprice", columnDefinition = "MEDIUMTEXT")
    private long lprice;

    @Column(name = "hprice", columnDefinition = "MEDIUMTEXT")
    private long hprice;

    @Column(name = "mallName", columnDefinition = "MEDIUMTEXT")
    private String mallName;

    @Column(name = "productType", columnDefinition = "MEDIUMTEXT")
    private int productType;

    @Column(name = "maker", columnDefinition = "MEDIUMTEXT")
    private String maker;

    @Column(name = "brand", columnDefinition = "MEDIUMTEXT")
    private String brand;

    public NaverProduct(NaverProductRequestDTO productRequestDTO) {
        this.id = productRequestDTO.getProductId();
        this.title = productRequestDTO.getTitle();
        this.link = productRequestDTO.getLink();
        this.image = productRequestDTO.getImage();
        this.lprice = productRequestDTO.getLprice();
        this.hprice = productRequestDTO.getHprice();
        this.mallName = productRequestDTO.getMallName();
        this.productType = productRequestDTO.getProductType();
        this.maker = productRequestDTO.getMaker();
        this.brand = productRequestDTO.getBrand();
    }
}
