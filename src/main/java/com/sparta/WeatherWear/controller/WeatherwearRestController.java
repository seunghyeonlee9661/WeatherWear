package com.sparta.WeatherWear.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.WeatherWear.dto.clothes.ClothesRequestDTO;
import com.sparta.WeatherWear.dto.clothes.ClothesResponseDTO;
import com.sparta.WeatherWear.dto.user.UserRequestDTO;
import com.sparta.WeatherWear.dto.wishlist.NaverProductRequestDTO;
import com.sparta.WeatherWear.dto.wishlist.NaverProductResponseDTO;
import com.sparta.WeatherWear.dto.wishlist.WishlistResponseDTO;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import com.sparta.WeatherWear.service.KakaoLoginService;
import com.sparta.WeatherWear.service.NaverShoppingService;
import com.sparta.WeatherWear.service.WeatherwearService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
/*
작성자 : 이승현
사용자 관련 서비스 API 처리
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WeatherwearRestController {

    private final WeatherwearService weatherwearService;
    private final NaverShoppingService naverShoppingService;
    private final KakaoLoginService kakaoLoginService;

    /*______________________User_______________________*/

    /* 사용자 정보 추가 */
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRequestDTO requestDTO) {
        return weatherwearService.createUser(requestDTO);
    }

    /* 사용자 정보 수정 */
    @PutMapping("/user")
    public ResponseEntity<String>  updateUserInfo(@RequestBody @Valid UserRequestDTO requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return weatherwearService.updateUserInfo(userDetails,requestDTO);
    }

    /* 사용자 비밀번호 수정 */
    @PutMapping("/user/password")
    public ResponseEntity<String>  updateUserPassword(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return weatherwearService.updateUserPassword(userDetails,request.get("password"));
    }

    /* 사용자 이미지 수정 */
    @PutMapping("/user/image")
    public ResponseEntity<String>  updateUserImage(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return weatherwearService.updateUserImage(userDetails,file);
    }

    /* 사용자 정보 삭제 */
    @DeleteMapping("/user")
    public ResponseEntity<String>  removeUser(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return weatherwearService.removeUser(userDetails,request.get("password"));
    }


    /*______________________Kakao_______________________*/

    @GetMapping("/kakao/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return kakaoLoginService.kakaoLogin(code,response);
    }

    /*______________________Clothes_______________________*/

    /* 옷 목록 불러오기 */
    @GetMapping("/clothes")
    public ResponseEntity<List<ClothesResponseDTO>> createClothes(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "type", required = false, defaultValue = "") String type, @RequestParam(value = "color", required = false, defaultValue = "") String color, @RequestParam(value = "page", defaultValue = "0") int page) {
        return weatherwearService.getClotheList(userDetails,page,type,color);
    }

    /* 옷 아이템 불러오기 */
    @GetMapping("/clothes/{id}")
    public ResponseEntity<ClothesResponseDTO> createClothes(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable("id") long id) {
        return weatherwearService.getClothe(userDetails,id);
    }

    /* 옷 정보 추가 */
    @PostMapping("/clothes")
    public ResponseEntity<String> createClothes(@RequestPart("clothesRequestDTO") @Validated ClothesRequestDTO clothesRequestDTO,@RequestPart(value = "file" , required = false) MultipartFile file, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return weatherwearService.createClothes(userDetails,clothesRequestDTO,file);
    }

    /* 옷 정보 삭제 */
    @DeleteMapping("/clothes/{id}")
    public ResponseEntity<String> removeClothes(@PathVariable("id") long id,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return weatherwearService.removeClothes(userDetails,id);
    }

    /*______________________NaverShoping_______________________*/

    /* 네이버 쇼핑 불러오기 */
    @GetMapping("/naver/shopping")
    public ResponseEntity<List<NaverProductResponseDTO>> findNaverProduct(@RequestParam(value = "query", required = true) String query,@RequestParam(value = "display", defaultValue = "10") int display) {
        return ResponseEntity.ok(naverShoppingService.searchProducts(query,display));
    }

    /*______________________Wishlist_______________________*/

    /* 위시리스트 불러오기 */
    @GetMapping("/wishlist")
    public ResponseEntity<List<WishlistResponseDTO>> findWishlist(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "page", defaultValue = "0") int page) {
        return weatherwearService.getWishlist(userDetails,page);
    }

    /* 위시리스트 추가하기 */
    @PostMapping("/wishlist")
    public ResponseEntity<String> createWishlist(@RequestBody @Valid NaverProductRequestDTO productRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return weatherwearService.createWishlist(productRequestDTO,userDetails);
    }

    /* 옷 정보 삭제 */
    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<String> removeWishlist(@PathVariable("id") long id,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return weatherwearService.removeWishlist(userDetails,id);
    }

    /*_________________________Recommend___________________*/
    

}
