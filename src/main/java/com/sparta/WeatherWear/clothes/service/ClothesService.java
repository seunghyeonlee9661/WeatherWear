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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
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
        // 페이지네이션 8개 아이템
        Pageable pageable = PageRequest.of(page, 8);

        // 사용자의 전체 옷 목록을 받아옵니다.
        List<Clothes> clothesList = clothesRepository.findByUserId(userDetails.getUser().getId());

        // 필터링을 적용합니다 : 없으면 전체 선택
        List<Clothes> filteredClothes = clothesList.stream()
                .filter(clothes -> (type == null || type.isEmpty() || clothes.getType().name().equalsIgnoreCase(type)))
                .filter(clothes -> (color == null || color.isEmpty() || clothes.getColor().name().equalsIgnoreCase(color)))
                .collect(Collectors.toList());
        // 페이지네이션을 적용합니다.
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredClothes.size());
        Page<Clothes> clothesPage = new PageImpl<>(filteredClothes.subList(start, end), pageable, filteredClothes.size());

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
        // 타입과 색상을 기준으로 옷 정보를 추가합니다.
        Clothes savedClothes = clothesRepository.save( new Clothes(color,type,userDetails.getUser()));
        // 파일이 있을 경우 저장하고 옷 정보에 추가합니다.
        if(file != null){
            File webPFile = imageTransformService.convertToWebP(file);
            String imageUrl = s3Service.uploadFile(webPFile);
            savedClothes.updateImage(imageUrl);
        }
        return ResponseEntity.ok().body("Clothes created successfully");
    }

    /* 옷 수정 */
    @Transactional
    public ResponseEntity<String> updateClothes(UserDetailsImpl userDetails,Long id,ClothesColor color, ClothesType type,boolean deleteImage,MultipartFile file) throws IOException {
        // 타입과 색상을 기준으로 옷 정보를 추가합니다.
        Clothes clothes = clothesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Clothes"));
        if(!clothes.getUser().getId().equals(userDetails.getUser().getId())) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 데이터가 아닙니다.");
        // 파일이 있을 경우 저장하고 옷 정보에 추가합니다.
        String url = null;
        if(file == null){
            if(deleteImage) s3Service.deleteFileByUrl(clothes.getImage());
        }else{
            if(clothes.getImage() != null){
                s3Service.deleteFileByUrl(clothes.getImage());
            }
            File webPFile = imageTransformService.convertToWebP(file);
            url = s3Service.uploadFile(webPFile);
        }
        clothes.update(color,type,url);
        return ResponseEntity.ok().body("Clothes created successfully");
    }


    /* 옷 삭제 */
    @Transactional
    public ResponseEntity<String> removeClothes(UserDetailsImpl userDetails, long id) throws IOException {
        // 옷 객체 검색
        Clothes clothes = clothesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Clothes"));
        // 사용자와 옷의 등록자가 일치하는지 확인
        if(!clothes.getUser().getId().equals(userDetails.getUser().getId()))  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 옷장 아이템이 아닙니다.");
        // 옷 이미지가 있을 경우 해당 이미지 삭제
        if(!clothes.getImage().isEmpty()) s3Service.deleteFileByUrl(clothes.getImage());
        // 삭제
        clothesRepository.delete(clothes);
        return ResponseEntity.ok("Clothes delete successfully");
    }

}
