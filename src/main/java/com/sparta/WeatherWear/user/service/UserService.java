package com.sparta.WeatherWear.user.service;

import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.clothes.dto.ClothesResponseDTO;
import com.sparta.WeatherWear.clothes.entity.Clothes;
import com.sparta.WeatherWear.clothes.repository.ClothesRepository;
import com.sparta.WeatherWear.global.service.ImageService;
import com.sparta.WeatherWear.user.dto.UserPasswordUpdateRequestDTO;
import com.sparta.WeatherWear.user.dto.UserUpdateRequestDTO;
import com.sparta.WeatherWear.weather.service.WeatherService;
import com.sparta.WeatherWear.user.dto.UserCreateRequestDTO;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.user.repository.UserRepository;
import com.sparta.WeatherWear.weather.repository.AddressRepository;
import com.sparta.WeatherWear.wishlist.dto.NaverProductRequestDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ImageService imageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    /*______________________User_______________________*/

    /* 회원가입 */
    @Transactional
    public ResponseEntity<String> createUser(UserCreateRequestDTO userCreateRequestDTO){
        if (userRepository.findByEmail(userCreateRequestDTO.getEmail()).isPresent()) throw new IllegalArgumentException("중복된 Email 입니다.");
        if (!userCreateRequestDTO.getPassword().equals(userCreateRequestDTO.getPasswordCheck())) throw new IllegalArgumentException("비밀번호 확인이 일치하지 않습니다.");
        User user = new User(userCreateRequestDTO,passwordEncoder.encode(userCreateRequestDTO.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    /* 회원 탈퇴*/
    @Transactional
    public ResponseEntity<String> removeUser(UserDetailsImpl userDetails, String password) throws IOException {
        User user = userDetails.getUser();
        if(!passwordEncoder.matches(password,user.getPassword())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
        if(!user.getImage().isEmpty()){
            imageService.deleteImage(user.getImage()); // 이미지가 있으면 삭제 ** 카카오 계정일 경우 이미지 삭제 거절!
        }
        userRepository.delete(user);
        return ResponseEntity.ok().body("User deleted successfully");
    }

    /* 회원 수정 */
    @Transactional
    public ResponseEntity<String> updateUserInfo(UserDetailsImpl userDetails, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = userDetails.getUser();
        if(!passwordEncoder.matches(userUpdateRequestDTO.getPassword(),user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
        user.updateInfo(userUpdateRequestDTO);
        userRepository.save(user); // 트랜잭셔널 작동안함 고쳐야됨 ㅠㅠ
        return ResponseEntity.ok().body("User updated successfully");
    }

    /* 회원 비밀번호 수정 */
    @Transactional
    public ResponseEntity<String> updateUserPassword(UserDetailsImpl userDetails, UserPasswordUpdateRequestDTO userPasswordUpdateRequestDTO) {
        User user = userDetails.getUser();
        if(!userPasswordUpdateRequestDTO.getNewPassword().equals(userPasswordUpdateRequestDTO.getNewPasswordCheck()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 확인이 일치하지 않습니다.");
        if(!passwordEncoder.matches(userPasswordUpdateRequestDTO.getCurrentPassword(),user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
        user.updatePassword(passwordEncoder.encode(userPasswordUpdateRequestDTO.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().body("User updated successfully");
    }

    /* 회원 사진 수정 */
    @Transactional
    public ResponseEntity<String> updateUserImage(UserDetailsImpl userDetails, MultipartFile file) throws IOException {
        User user = userDetails.getUser(); // 사용자 확인
        String imageUrl = imageService.uploadImagefile("user/", String.valueOf(user.getId()),file);
        user.updateImage(imageUrl);
        userRepository.save(user);
        return ResponseEntity.ok("User image updated successfully");
    }
}
