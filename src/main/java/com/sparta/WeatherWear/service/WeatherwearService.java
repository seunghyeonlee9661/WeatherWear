package com.sparta.WeatherWear.service;

import com.sparta.WeatherWear.dto.clothes.ClothesRequestDTO;
import com.sparta.WeatherWear.dto.clothes.ClothesResponseDTO;
import com.sparta.WeatherWear.dto.user.UserRequestDTO;
import com.sparta.WeatherWear.dto.wishlist.NaverProductRequestDTO;
import com.sparta.WeatherWear.dto.wishlist.WishlistResponseDTO;
import com.sparta.WeatherWear.entity.*;
import com.sparta.WeatherWear.repository.*;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
/*
작성자 : 이승현
사용자 관련 서비스 처리
*/
@Service
public class WeatherwearService {

    private final UserRepository userRepository;
    private final ClothesRepository clothesRepository;
    private final WishlistRepository wishlistRepository;
    private final NaverProductRepository naverProductRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;


    public WeatherwearService(ClothesRepository clothesRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, WishlistRepository wishlistRepository, NaverProductRepository naverProductRepository, AddressRepository addressRepository, WeatherService weatherService, ImageService imageService) {
        this.clothesRepository = clothesRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.naverProductRepository = naverProductRepository;
        this.imageService = imageService;
    }

    /*______________________User_______________________*/

    /* 회원가입 */
    @Transactional
    public ResponseEntity<String> createUser(UserRequestDTO userCreateRequestDTO){
        if (userRepository.findByEmail(userCreateRequestDTO.getEmail()).isPresent()) throw new IllegalArgumentException("중복된 Email 입니다.");
        if (!userCreateRequestDTO.getPassword().equals(userCreateRequestDTO.getPasswordCheck())) throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        User user = new User(userCreateRequestDTO,passwordEncoder.encode(userCreateRequestDTO.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    /* 회원 탈퇴*/
    @Transactional
    public ResponseEntity<String> removeUser(UserDetailsImpl userDetails, String password){
        User user = userDetails.getUser();
        if(!passwordEncoder.matches(password,user.getPassword())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
        userRepository.delete(user);
        return ResponseEntity.ok().body("User deleted successfully");
    }

    /* 회원 수정 */
    @Transactional
    public ResponseEntity<String> updateUserInfo(UserDetailsImpl userDetails, UserRequestDTO userRequestDTO) {
        User user = userDetails.getUser();
        if(!passwordEncoder.matches(userRequestDTO.getPassword(),user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
        user.updateInfo(userRequestDTO);
        userRepository.save(user); // 트랜잭셔널 작동안함 고쳐야됨 ㅠㅠ
        return ResponseEntity.ok().body("User updated successfully");
    }

    /* 회원 비밀번호 수정 */
    @Transactional
    public ResponseEntity<String> updateUserPassword(UserDetailsImpl userDetails, String password) {
        User user = userDetails.getUser();
        if(!passwordEncoder.matches(password,user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
        user.updatePassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return ResponseEntity.ok().body("User updated successfully");
    }

    /* 회원 사진 수정 */
    @Transactional
    public ResponseEntity<String> updateUserImage(UserDetailsImpl userDetails, MultipartFile file) throws IOException {
        User user = userDetails.getUser(); // 사용자 확인
        String imageUrl = imageService.uploadImagefile("user/" + user.getId(),file,220,200,0.7);
        user.updateImage(imageUrl);
        userRepository.save(user);
        return ResponseEntity.ok("User image updated successfully");
    }

    /*______________________Clothes_______________________*/

    /* 옷 목록 불러오기 */
    public ResponseEntity<List<ClothesResponseDTO>> getClotheList(UserDetailsImpl userDetails, int page, String type, String color) {
        Pageable pageable = PageRequest.of(page, 8);
        // 전체 데이터 가져오기
        Page<Clothes> clothesPage = clothesRepository.findByUserId(userDetails.getUser().getId(), pageable);
        // 필터링 적용
        List<Clothes> filteredClothes = clothesPage.stream()
                .filter(clothes -> (type == null || type.trim().isEmpty() || clothes.getType().name().equalsIgnoreCase(type)))
                .filter(clothes -> (color == null || color.trim().isEmpty() || clothes.getColor().name().equalsIgnoreCase(color)))
                .toList();
        return ResponseEntity.ok(filteredClothes.stream().map(ClothesResponseDTO::new).collect(Collectors.toList()));
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
            String imageUrl = imageService.uploadImagefile("clothes/" + savedClothes.getId(),file,300,400,0.7);
            savedClothes.updateImage(imageUrl);
            clothesRepository.save(savedClothes);
        }
        return ResponseEntity.ok().body("Clothes created successfully");
    }

    /* 옷 삭제 */
    @Transactional
    public ResponseEntity<String> removeClothes(UserDetailsImpl userDetails, long id){
        Clothes clothes = clothesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No Clothes"));
        if(!clothes.getUser().getId().equals(userDetails.getUser().getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 옷장 아이템이 아닙니다.");
        clothesRepository.delete(clothes);
        return ResponseEntity.ok("Clothes delete successfully");
    }

    /*______________________Wishlist_______________________*/

    /* 위시리스트 불러오기 */
    public ResponseEntity<List<WishlistResponseDTO>> getWishlist(UserDetailsImpl userDetails, int page){
        Pageable pageable = PageRequest.of(page, 8);
        // 전체 데이터 가져오기
        Page<Wishlist> wishlistPage = wishlistRepository.findByUserId(userDetails.getUser().getId(), pageable);
        return ResponseEntity.ok(wishlistPage.stream().map(WishlistResponseDTO::new).collect(Collectors.toList()));
    }

    /* 위시리스트 추가 */
    @Transactional
    public ResponseEntity<String> createWishlist(NaverProductRequestDTO productRequestDTO,UserDetailsImpl userDetails){
        NaverProduct product = naverProductRepository.findById(productRequestDTO.getProductId()).orElseGet(() -> naverProductRepository.save(new NaverProduct(productRequestDTO)));
        wishlistRepository.save(new Wishlist(userDetails.getUser(), product));
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
