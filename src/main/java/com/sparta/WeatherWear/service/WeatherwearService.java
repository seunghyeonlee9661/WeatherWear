package com.sparta.WeatherWear.service;

import com.sparta.WeatherWear.dto.clothes.ClothesRequestDTO;
import com.sparta.WeatherWear.dto.clothes.ClothesResponseDTO;
import com.sparta.WeatherWear.dto.user.UserRequestDTO;
import com.sparta.WeatherWear.dto.wishlist.NaverProductRequestDTO;
import com.sparta.WeatherWear.dto.wishlist.WishlistResponseDTO;
import com.sparta.WeatherWear.entity.Clothes;
import com.sparta.WeatherWear.entity.NaverProduct;
import com.sparta.WeatherWear.entity.User;
import com.sparta.WeatherWear.entity.Wishlist;
import com.sparta.WeatherWear.repository.ClothesRepository;
import com.sparta.WeatherWear.repository.NaverProductRepository;
import com.sparta.WeatherWear.repository.UserRepository;
import com.sparta.WeatherWear.repository.WishlistRepository;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
/*
작성자 : 이승현
사용자 관련 서비스 처리
*/
@Service
public class WeatherwearService {


    @Value("${naver.client.id}")
    private String clientId;
    @Value("${naver.client.secret}")
    private String clientSecret;

    private final UserRepository userRepository;
    private final ClothesRepository clothesRepository;
    private final WishlistRepository wishlistRepository;
    private final NaverProductRepository naverProductRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final FTPService ftpService;


    public WeatherwearService(ClothesRepository clothesRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, WishlistRepository wishlistRepository, NaverProductRepository naverProductRepository, FTPService ftpService) {
        this.clothesRepository = clothesRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.wishlistRepository = wishlistRepository;
        this.naverProductRepository = naverProductRepository;
        this.ftpService = ftpService;
    }

    /*______________________User_______________________*/

    /* 회원 정보 호출 */
    public ResponseEntity<User> findUser(UserDetailsImpl userDetails){
        return ResponseEntity.status(HttpStatus.CREATED).body(userDetails.getUser());
    }

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
        String ftpFilename = String.valueOf(user.getId()); // 파일 이름 선정
        boolean uploaded = ftpService.uploadImageToFtp(ftpFilename,file); // 이미지 업로드 진행
        if (uploaded) { // 업로드 성공시 - URL 값을 user에 갱신
            String imageURL = "http://119.56.220.32/images/user/" + ftpFilename;
            user.updateImage(imageURL);
            userRepository.save(user);
            return ResponseEntity.ok().body("User image updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User image updated fail");
        }
    }

    /*______________________Clothes_______________________*/

    /* 옷 목록 불러오기 */
    public ResponseEntity<List<ClothesResponseDTO>> getClothes(UserDetailsImpl userDetails, int page, String type, String color) {
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

    /* 옷 추가 */
    @Transactional
    public ResponseEntity<String> createClothes(UserDetailsImpl userDetails, ClothesRequestDTO clothesRequestDTO){
        Clothes clothes = new Clothes(clothesRequestDTO,userDetails.getUser());
        clothesRepository.save(clothes);
        return ResponseEntity.ok("Clothes created successfully");
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

    /*______________________Naver_______________________*/

    // 네이버로부터 입력에 대한 검색 결과를 받아오는 기능
    public ResponseEntity<String> searchProducts(String query, int display, int start, String sort, String filter){
        /* 헤더 정의 */
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        /* url 정의 */
        String apiUrl = "https://openapi.naver.com/v1/search/shop.json?query=" + query+ "&display=" + display + "&start=" + start + "&sort=" + sort + "&filter=" + filter;
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return new RestTemplate().exchange(apiUrl, HttpMethod.GET, entity, String.class);
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
