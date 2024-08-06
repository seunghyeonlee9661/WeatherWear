package com.sparta.WeatherWear.clothes.controller;

import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.clothes.dto.ClothesResponseDTO;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.clothes.service.ClothesService;
import com.sparta.WeatherWear.wishlist.dto.NaverProductResponseDTO;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.wishlist.service.NaverShoppingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
/*
작성자 : 이승현
사용자 관련 서비스 API 처리
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ClothesController {


    private final ClothesService clothesService;


    /*______________________Clothes_______________________*/

    /* 옷 목록 불러오기 */
    @GetMapping("/clothes")
    public ResponseEntity<Page<ClothesResponseDTO>> createClothes(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "type", required = false, defaultValue = "") String type, @RequestParam(value = "color", required = false, defaultValue = "") String color, @RequestParam(value = "page", defaultValue = "0") int page) {
        return clothesService.getClotheList(userDetails,page,type,color);
    }

    /* 옷 아이템 불러오기 */
    @GetMapping("/clothes/{id}")
    public ResponseEntity<ClothesResponseDTO> createClothes(@PathVariable("id") long id) {
        return clothesService.getClothe(id);
    }

    /* 옷 정보 추가 */
    @PostMapping("/clothes")
    public ResponseEntity<String> createClothes(
            @RequestPart("color") String color,
            @RequestPart("type") String type,
            @RequestPart(value = "file" , required = false) MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return clothesService.createClothes(userDetails,ClothesColor.valueOf(color),ClothesType.valueOf(type),file);
    }

    /* 옷 정보 수정 */
    @PutMapping("/clothes")
    public ResponseEntity<String> updateClothes(
            @RequestPart("color")long id,
            @RequestPart("color") String color,
            @RequestPart("type") String type,
            @RequestPart(value = "file" , required = false) MultipartFile file,@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return clothesService.updateClothes(userDetails,id,ClothesColor.valueOf(color),ClothesType.valueOf(type),file);
    }

    /* 옷 정보 삭제 */
    @DeleteMapping("/clothes/{id}")
    public ResponseEntity<String> removeClothes(@PathVariable("id") long id,@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return clothesService.removeClothes(userDetails,id);
    }
}
