package com.sparta.WeatherWear.user.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.WeatherWear.board.dto.BoardCreateResponseDto;
import com.sparta.WeatherWear.board.dto.SimpleBoardResponseDTO;
import com.sparta.WeatherWear.global.dto.ResponseDTO;
import com.sparta.WeatherWear.user.dto.UserCreateRequestDTO;
import com.sparta.WeatherWear.user.dto.UserPasswordUpdateRequestDTO;
import com.sparta.WeatherWear.user.dto.UserResponseDTO;
import com.sparta.WeatherWear.user.service.RecommendService;
import com.sparta.WeatherWear.user.service.UserService;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.user.service.KakaoLoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@Tag(name = "사용자 API", description = "사용자 관련 API")
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final RecommendService recommendService;

    /* 사용자 정보 요청 */
    @GetMapping("/users/me")
    public ResponseEntity<UserResponseDTO> findUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(new UserResponseDTO(userDetails.getUser()));
    }

    /* 사용자 게시물 요청 */
    @GetMapping("/users/boards")
    public ResponseEntity<Page<SimpleBoardResponseDTO>> findUserBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                      @RequestParam(value= "page", required = false, defaultValue="0") int page,
                                                                      @RequestParam(value= "pty",required = false) Integer pty,
                                                                      @RequestParam(value= "sky",required = false) Integer sky,
                                                                      @RequestParam(value= "keyword",required = false) String keyword){
        return userService.findUserBoard(userDetails,page,pty,sky,keyword);
    }

    /* 사용자 정보 추가 */
    @PostMapping("/users")
    public ResponseEntity<String> createUser( @Valid @RequestBody UserCreateRequestDTO requestDTO) {
        return userService.createUser(requestDTO);
    }

    // FIXME : 데이터 DTO로 처리할 수 있도록 개선 필요
    /* 사용자 정보 수정 */
    @PutMapping("/users")
    public ResponseEntity<String>  updateUserInfo(
            @RequestParam("nickname") String nickname,
            @RequestParam("deleteImage") boolean deleteImage,
            @RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.updateUserInfo(userDetails,nickname,deleteImage,file);
    }

    /* 사용자 비밀번호 수정 */
    @PutMapping("/users/password")
    public ResponseEntity<String>  updateUserPassword(@RequestBody @Valid UserPasswordUpdateRequestDTO userPasswordUpdateRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.updateUserPassword(userDetails,userPasswordUpdateRequestDTO);
    }
    /* 사용자 정보 삭제 */
    @DeleteMapping("/users")
    public ResponseEntity<String>  removeUser(@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return userService.removeUser(userDetails);
    }

    /* 추천 아이템들 불러오기 */
    @GetMapping("/recommends")
    public ResponseEntity<List<List<? extends ResponseDTO>>> getRecommend(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "id") Long id) throws JsonProcessingException {
        return ResponseEntity.ok(recommendService.getRecommends(userDetails,id));
    }

    /* 추천 아이템 중 위시리스트 삭제하기 */
    @DeleteMapping("/recommends/wishlist/{product_id}")
    public ResponseEntity<String> removeWishlistAtRecommend(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("product_id") Long product_id) {
        return recommendService.removeWishlistByProductId(userDetails,product_id);
    }
}
