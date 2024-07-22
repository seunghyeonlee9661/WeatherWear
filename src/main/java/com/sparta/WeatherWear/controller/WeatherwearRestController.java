package com.sparta.WeatherWear.controller;

import com.sparta.WeatherWear.dto.clothes.ClothesRequestDTO;
import com.sparta.WeatherWear.dto.clothes.ClothesResponseDTO;
import com.sparta.WeatherWear.dto.user.UserRequestDTO;
import com.sparta.WeatherWear.dto.wishlist.NaverProductRequestDTO;
import com.sparta.WeatherWear.dto.wishlist.WishlistResponseDTO;
import com.sparta.WeatherWear.entity.User;
import com.sparta.WeatherWear.enums.ClothesColor;
import com.sparta.WeatherWear.enums.ClothesType;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import com.sparta.WeatherWear.service.WeatherwearService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WeatherwearRestController {
    private final WeatherwearService service;

    /*______________________User_______________________*/

    /* 사용자 정보 확인 */
    @GetMapping("/user")
    public ResponseEntity<User> findUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return service.findUser(userDetails);
    }

    /* 사용자 정보 추가 */
    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRequestDTO requestDTO) {
        return service.createUser(requestDTO);
    }

    /* 사용자 정보 수정 */
    @PutMapping("/user")
    public ResponseEntity<String>  removeUser(@RequestBody @Valid UserRequestDTO requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return service.updateUser(userDetails,requestDTO);
    }

    /* 사용자 정보 삭제 */
    @DeleteMapping("/user")
    public ResponseEntity<String>  removeUser(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return service.removeUser(userDetails,request.get("password"));
    }

    /*______________________Clothes_______________________*/

    /* 옷 정보 불러오기 */
    @GetMapping("/clothes")
    public ResponseEntity<List<ClothesResponseDTO>> createClothes(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "type", required = false, defaultValue = "") String type, @RequestParam(value = "color", required = false, defaultValue = "") String color, @RequestParam(value = "page", defaultValue = "0") int page) {
        return service.getClothes(userDetails,page,type,color);
    }

    /* 옷 정보 추가 */
    @PostMapping("/clothes")
    public ResponseEntity<String> createClothes(@RequestBody @Valid ClothesRequestDTO clothesRequestDTO,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return service.createClothes(userDetails,clothesRequestDTO);
    }

    /* 옷 정보 삭제 */
    @DeleteMapping("/clothes/{id}")
    public ResponseEntity<String> removeClothes(@PathVariable("id") long id) {
        return service.removeClothes(id);
    }

    /*______________________NaverShoping_______________________*/

    /* 옷 정보 불러오기 */
    @GetMapping("/naver/shopping")
    public ResponseEntity<String> findNaverProduct(@RequestParam(value = "query", required = true) String query,
                                                @RequestParam(value = "display", required = false,defaultValue="10") int display,
                                                @RequestParam(value = "start", required = false,defaultValue="1") int start,
                                                @RequestParam(value = "sort", required = false,defaultValue="sim") String sort,
                                                @RequestParam(value = "filter", required = false,defaultValue="") String filter) {
        return service.searchProducts(query,display,start,sort,filter);
    }

    /*______________________Wishlist_______________________*/

    /* 위시리스트 불러오기 */
    @GetMapping("/wishlist")
    public ResponseEntity<List<WishlistResponseDTO>> findWishlist(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "page", defaultValue = "0") int page) {
        return service.getWishlist(userDetails,page);
    }

    /* 위시리스트 추가하기 */
    @PostMapping("/wishlist/{product_id}")
    public ResponseEntity<String> createWishlist(@RequestBody @Valid NaverProductRequestDTO productRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return service.createWishlist(productRequestDTO,userDetails);
    }

}
