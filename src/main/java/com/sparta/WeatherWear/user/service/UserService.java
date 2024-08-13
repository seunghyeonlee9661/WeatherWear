package com.sparta.WeatherWear.user.service;

import com.sparta.WeatherWear.board.dto.BoardCreateResponseDto;
import com.sparta.WeatherWear.board.dto.SimpleBoardResponseDTO;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.repository.BoardRepository;
import com.sparta.WeatherWear.global.service.ImageTransformService;
import com.sparta.WeatherWear.global.service.S3Service;
import com.sparta.WeatherWear.user.dto.UserPasswordUpdateRequestDTO;
import com.sparta.WeatherWear.user.dto.UserCreateRequestDTO;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.user.repository.UserRepository;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/*
작성자 : 이승현
사용자 관련 서비스 처리
*/
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final ImageTransformService imageTransformService;

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
    public ResponseEntity<String> removeUser(UserDetailsImpl userDetails) throws IOException {
        User user = userDetails.getUser();
        // 카카오 계정이 아니고, 이미지가 있는 경우에만 이미지 삭제
        if (user.getKakaoId() == null && user.getImage() != null) {
            s3Service.deleteFileByUrl(user.getImage());
        }
        userRepository.delete(user);
        return ResponseEntity.ok().body("User deleted successfully");
    }

    /* 회원 수정 */
    @Transactional
    public ResponseEntity<String> updateUserInfo(UserDetailsImpl userDetails, String nickname,boolean deleteImage, MultipartFile file) throws IOException {
        User user = userDetails.getUser();
        // 사용자의 기존 nickname과 다르면서 현재 다른 사람이 nickname을 쓰고 있는 경우 : nickname 중복
        if(!user.getNickname().equals(nickname) && userRepository.existsByNickname(nickname)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already taken.");

        String url = user.getImage();
        if(file == null || file.isEmpty()){
            if(deleteImage) {
                url = null;
                s3Service.deleteFileByUrl(user.getImage());
            }
        }else{
            if(user.getImage() != null) s3Service.deleteFileByUrl(user.getImage());
            File webPFile = imageTransformService.convertToWebP(file);
            url = s3Service.uploadFile(webPFile);
        }
        user.updateInfo(nickname,url);
        userRepository.save(user);
        return ResponseEntity.ok().body("User updated successfully");
    }

    /* 회원 비밀번호 수정 */
    @Transactional
    public ResponseEntity<String> updateUserPassword(UserDetailsImpl userDetails, UserPasswordUpdateRequestDTO userPasswordUpdateRequestDTO) {
        User user = userDetails.getUser();
        if(!userPasswordUpdateRequestDTO.getNewPassword().equals(userPasswordUpdateRequestDTO.getNewPasswordCheck()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("새 비밀번호 확인이 일치하지 않습니다.");
        if(!passwordEncoder.matches(userPasswordUpdateRequestDTO.getCurrentPassword(),user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("현재 비밀번호가 올바르지 않습니다.");
        user.updatePassword(passwordEncoder.encode(userPasswordUpdateRequestDTO.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().body("User updated successfully");
    }

    /* 사용자의 게시물 검색 기능 */
    public ResponseEntity<Page<SimpleBoardResponseDTO>> findUserBoard(UserDetailsImpl userDetails, int page, Integer pty, Integer sky, String keyword){
        Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Order.desc("id")));
        Page<Board> boardPage  = boardRepository.findByUserId(userDetails.getUser().getId(),pty,sky,keyword,pageable);
        return ResponseEntity.ok(boardPage.map(SimpleBoardResponseDTO::new));
    }
}
