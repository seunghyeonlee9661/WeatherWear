package com.sparta.WeatherWear.wishlist.controller;

import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.wishlist.dto.WishlistRequestDTO;
import com.sparta.WeatherWear.wishlist.dto.NaverProductResponseDTO;
import com.sparta.WeatherWear.wishlist.dto.WishlistResponseDTO;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.wishlist.service.NaverShoppingService;
import com.sparta.WeatherWear.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
작성자 : 이승현
사용자 관련 서비스 API 처리
 */
@RequiredArgsConstructor
@RestController
@Tag(name = "위시리스트 API", description = "옷 위시리스트")
@RequestMapping("/api")
public class WishlistController {

    private final WishlistService wishlistService;
    private final NaverShoppingService naverShoppingService;

    /* 위시리스트 불러오기 */
    @GetMapping("/wishlist")
    public ResponseEntity<Page<WishlistResponseDTO>> findWishlist(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "type", required = false, defaultValue = "") String type) {
        return wishlistService.getWishlist(userDetails,page, type);
    }

    /* 위시리스트 추가하기 */
    @PostMapping("/wishlist")
    public ResponseEntity<String> createWishlist(@RequestBody @Valid WishlistRequestDTO productRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return wishlistService.createWishlist(productRequestDTO,userDetails);
    }

    /* 옷 정보 삭제 */
    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<String> removeWishlist(@PathVariable("id") long id,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return wishlistService.removeWishlist(userDetails,id);
    }
}
