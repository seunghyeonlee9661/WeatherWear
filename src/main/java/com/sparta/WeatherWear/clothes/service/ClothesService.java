package com.sparta.WeatherWear.clothes.service;

import com.sparta.WeatherWear.clothes.dto.ClothesResponseDTO;
import com.sparta.WeatherWear.clothes.entity.Clothes;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.clothes.repository.ClothesRepository;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.global.service.ImageTransformService;
import com.sparta.WeatherWear.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
/*
작성자 : 이승현
사용자가 가지고 있는 옷 정보를 처리하는 Service
*/
@Service
@RequiredArgsConstructor
public class ClothesService {
    private final ClothesRepository clothesRepository;
    private final S3Service s3Service;
    private final ImageTransformService imageTransformService;

    /* 옷 목록 불러오기 : 페이지네이션과 타입, 색상에 따라 필터링 됩니다. */
    public ResponseEntity<Page<ClothesResponseDTO>> getClotheList(UserDetailsImpl userDetails, int page, String type, String color) {
        // 필터링 및 정렬을 데이터베이스 레벨에서 처리
        Page<Clothes> clothesPage = clothesRepository.findByUserIdAndFilters(
                userDetails.getUser().getId(),
                (type != null && !type.isEmpty()) ? ClothesType.valueOf(type.toUpperCase()) : null,
                (color != null && !color.isEmpty()) ? ClothesColor.valueOf(color.toUpperCase()) : null,
                PageRequest.of(page, 8));
        return ResponseEntity.ok(clothesPage.map(ClothesResponseDTO::new));
    }

    /* 단일 옷 아이템 정보를 불러오기 */
    public ResponseEntity<ClothesResponseDTO> getClothe(long id) {
        Clothes clothes = clothesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Clothes"));
        return ResponseEntity.ok(new ClothesResponseDTO(clothes));
    }

    /* 옷 추가 */
    @Transactional
    public ResponseEntity<String> createClothes(UserDetailsImpl userDetails, ClothesColor color, ClothesType type, MultipartFile file) throws IOException {
        if(file != null) {
            String imageUrl = s3Service.uploadFile(imageTransformService.convertToWebP(file)); // 이미지를 WEBP로 변환하고 url 업로드
            clothesRepository.save(new Clothes(color, type, userDetails.getUser(), imageUrl));
            return ResponseEntity.ok().body("Clothes created successfully");
        }else{
            // 이미지 파일을 업로드 하지 않은 경우 : 이미지 파일 요청
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Clothes Image Required");
        }
    }


    /* 옷 수정 */
    @Transactional
    public ResponseEntity<String> updateClothes(UserDetailsImpl userDetails,Long id,ClothesColor color, ClothesType type,MultipartFile file) throws IOException {
        // 타입과 색상을 기준으로 옷 정보를 추가합니다.
        Clothes clothes = clothesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Clothes"));
        // 사용자와 옷의 등록자가 일치하는지 확인
        if(!clothes.getUser().getId().equals(userDetails.getUser().getId())) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 데이터가 아닙니다.");
        // 파일이 있을 경우 저장하고 옷 정보에 추가합니다.
        String url = clothes.getImage();
        if(file != null && !file.isEmpty()){ // 파일이 있는 경우 : 이미지 수정
            if(clothes.getImage() != null) s3Service.deleteFileByUrl(clothes.getImage()); // 기존 이미지 제거
            url = s3Service.uploadFile( imageTransformService.convertToWebP(file)); // 이미지를 WEBP로 변환하고 url 업로드
        }
        clothes.update(color,type,url);
        return ResponseEntity.ok().body("Clothes created successfully");
    }

    /* 옷 삭제 */
    @Transactional
    public ResponseEntity<String> removeClothes(UserDetailsImpl userDetails, long id) throws IOException {
        Clothes clothes = clothesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Clothes"));// 옷 객체 검색
        // 사용자와 옷의 등록자가 일치하는지 확인
        if(!clothes.getUser().getId().equals(userDetails.getUser().getId()))  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 옷장 아이템이 아닙니다.");
        s3Service.deleteFileByUrl(clothes.getImage()); // 옷 이미지 삭제
        clothesRepository.delete(clothes);// 삭제
        return ResponseEntity.ok("Clothes delete successfully");
    }
}
