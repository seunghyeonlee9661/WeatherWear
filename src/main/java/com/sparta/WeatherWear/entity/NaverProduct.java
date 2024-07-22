package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.dto.wishlist.NaverProductRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class NaverProduct {

    @Id
    private Long id;

    @Column(name = "title", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String title;

    @Column(name = "link", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String link;

    @Column(name = "image", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String image;

    @Column(name = "lprice", columnDefinition = "MEDIUMTEXT", nullable = false)
    private long lprice;

    @Column(name = "hprice", columnDefinition = "MEDIUMTEXT", nullable = false)
    private long hprice;

    @Column(name = "mallName", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String mallName;

    @Column(name = "productType", columnDefinition = "MEDIUMTEXT", nullable = false)
    private int productType;

    @Column(name = "maker", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String maker;

    @Column(name = "brand", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String brand;

    public NaverProduct(NaverProductRequestDTO productRequestDTO) {
        this.id = productRequestDTO.getId();
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
