package com.sparta.WeatherWear.wishlist.service;

import com.sparta.WeatherWear.wishlist.dto.WishlistRequestDTO;
import com.sparta.WeatherWear.wishlist.dto.WishlistResponseDTO;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.wishlist.entity.NaverProduct;
import com.sparta.WeatherWear.wishlist.entity.Wishlist;
import com.sparta.WeatherWear.wishlist.repository.NaverProductRepository;
import com.sparta.WeatherWear.wishlist.repository.WishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
/*
작성자 : 이승현
사용자 관련 서비스 처리
*/
@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final NaverProductRepository naverProductRepository;

    public WishlistService(WishlistRepository wishlistRepository, NaverProductRepository naverProductRepository) {
        this.wishlistRepository = wishlistRepository;
        this.naverProductRepository = naverProductRepository;
    }

    /* 위시리스트 불러오기 */
    public ResponseEntity<List<WishlistResponseDTO>> getWishlist(UserDetailsImpl userDetails, int page){
        Pageable pageable = PageRequest.of(page, 8);
        // 전체 데이터 가져오기
        Page<Wishlist> wishlistPage = wishlistRepository.findByUserId(userDetails.getUser().getId(), pageable);
        return ResponseEntity.ok(wishlistPage.stream().map(WishlistResponseDTO::new).collect(Collectors.toList()));
    }

    /* 위시리스트 추가 */
    @Transactional
    public ResponseEntity<String> createWishlist(WishlistRequestDTO productRequestDTO, UserDetailsImpl userDetails){
        NaverProduct product = naverProductRepository.findById(productRequestDTO.getProductId()).orElseGet(() -> naverProductRepository.save(new NaverProduct(productRequestDTO)));
        wishlistRepository.save(new Wishlist(userDetails.getUser(), product,productRequestDTO.getType()));
        return ResponseEntity.ok("Wishlist created successfully");
    }

    /* 위시리스트 삭제 */
    @Transactional
    public ResponseEntity<String> removeWishlist(UserDetailsImpl userDetails, long id){
        Wishlist wishlist = wishlistRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Wishlist"));
        if(!wishlist.getUser().getId().equals(userDetails.getUser().getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 위시리스트 아이템이 아닙니다.");
        wishlistRepository.delete(wishlist);
        return ResponseEntity.ok("Wishlist delete successfully");
    }
}
