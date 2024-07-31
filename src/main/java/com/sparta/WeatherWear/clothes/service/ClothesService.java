package com.sparta.WeatherWear.clothes.service;

import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.clothes.dto.ClothesResponseDTO;
import com.sparta.WeatherWear.clothes.entity.Clothes;
import com.sparta.WeatherWear.clothes.repository.ClothesRepository;
import com.sparta.WeatherWear.global.service.ImageService;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
/*
작성자 : 이승현
사용자 관련 서비스 처리
*/
@Service
public class ClothesService {

    private final ClothesRepository clothesRepository;
    private final ImageService imageService;

    public ClothesService(ClothesRepository clothesRepository, ImageService imageService) {
        this.clothesRepository = clothesRepository;
        this.imageService = imageService;
    }

    /* 옷 목록 불러오기 */
    public ResponseEntity<Page<ClothesResponseDTO>> getClotheList(UserDetailsImpl userDetails, int page, String type, String color) {
        Pageable pageable = PageRequest.of(page, 8);

        List<Clothes> clothesList = clothesRepository.findByUserId(userDetails.getUser().getId());

        // 필터링 적용
        List<Clothes> filteredClothes = clothesList.stream()
                .filter(clothes -> (type == null || type.trim().isEmpty() || clothes.getType().name().equalsIgnoreCase(type)))
                .filter(clothes -> (color == null || color.trim().isEmpty() || clothes.getColor().name().equalsIgnoreCase(color)))
                .collect(Collectors.toList());

        // 페이지네이션 적용
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredClothes.size());
        Page<Clothes> clothesPage = new PageImpl<>(filteredClothes.subList(start, end), pageable, filteredClothes.size());

        return ResponseEntity.ok(clothesPage.map(ClothesResponseDTO::new));
    }

    /* 옷 정보 불러오기 */
    public ResponseEntity<ClothesResponseDTO> getClothe(UserDetailsImpl userDetails, long id) {
        Clothes clothes = clothesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Clothes"));
        return ResponseEntity.ok(new ClothesResponseDTO(clothes));
    }


    /* 옷 추가 */
    @Transactional
    public ResponseEntity<String> createClothes(UserDetailsImpl userDetails, ClothesRequestDTO clothesRequestDTO,MultipartFile file) throws IOException {
        Clothes savedClothes = clothesRepository.save( new Clothes(clothesRequestDTO,userDetails.getUser()));
        if(file != null){
            String imageUrl = imageService.uploadImagefile("clothes/", String.valueOf(savedClothes.getId()),file);
            savedClothes.updateImage(imageUrl);
            clothesRepository.save(savedClothes);
        }
        return ResponseEntity.ok().body("Clothes created successfully");
    }

    /* 옷 삭제 */
    @Transactional
    public ResponseEntity<String> removeClothes(UserDetailsImpl userDetails, long id) throws IOException {
        Clothes clothes = clothesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Clothes"));
        if(!clothes.getUser().getId().equals(userDetails.getUser().getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 옷장 아이템이 아닙니다.");
        if(!clothes.getImage().isEmpty()){
            imageService.deleteImage(clothes.getImage()); // 이미지가 있으면 삭제
        }
        clothesRepository.delete(clothes);
        return ResponseEntity.ok("Clothes delete successfully");
    }

}
