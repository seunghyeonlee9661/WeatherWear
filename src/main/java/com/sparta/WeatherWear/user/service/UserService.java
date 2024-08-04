package com.sparta.WeatherWear.user.service;

import com.sparta.WeatherWear.board.dto.BoardListResponseDTO;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.repository.BoardRepository;
import com.sparta.WeatherWear.global.service.ImageService;
import com.sparta.WeatherWear.user.dto.RecommendBoardResponseDTO;
import com.sparta.WeatherWear.user.dto.UserPasswordUpdateRequestDTO;
import com.sparta.WeatherWear.user.dto.UserCreateRequestDTO;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.user.repository.UserRepository;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final ImageService imageService;


    public ResponseEntity<List<BoardListResponseDTO>> findUserBoard(UserDetailsImpl userDetails){
        List<Board> boardList = boardRepository.findByUserId(userDetails.getUser().getId());
        return ResponseEntity.ok(boardList.stream().map(BoardListResponseDTO::new).toList());
    }


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
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
        }
        // 카카오 계정이 아니고, 이미지가 있는 경우에만 이미지 삭제
        if (user.getKakaoId() == null && user.getImage() != null) {
            imageService.deleteImage(user.getImage());
        }
        userRepository.delete(user);
        return ResponseEntity.ok().body("User deleted successfully");
    }

    /* 회원 수정 */
    @Transactional
    public ResponseEntity<String> updateUserInfo(UserDetailsImpl userDetails, String nickname,String url, MultipartFile file) throws IOException {
        User user = userDetails.getUser();
        // 사용자의 기존 nickname과 다르면서 현재 다른 사람이 nickname을 쓰고 있는 경우 : nickname 중복
        if(!user.getNickname().equals(nickname) && userRepository.existsByNickname(nickname)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Nickname is already taken.");
        if (url.isEmpty()) {
            if (file.isEmpty()) { // 사용자가 기존 사진 삭제한 경우
                if (user.getKakaoId() == null && user.getImage() != null) imageService.deleteImage(user.getImage());
                url = null;
            } else { // 사용자가 새롭게 사진을 올리는 경우
                url = imageService.uploadImagefile("user/", String.valueOf(user.getId()), file);
            }
        } else { // imageUrl: prevImageUrl
            if (!file.isEmpty()) url = imageService.uploadImagefile("user/", String.valueOf(user.getId()), file);// 사용자가 이미지를 수정하는 경우
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 확인이 일치하지 않습니다.");
        if(!passwordEncoder.matches(userPasswordUpdateRequestDTO.getCurrentPassword(),user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
        user.updatePassword(passwordEncoder.encode(userPasswordUpdateRequestDTO.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().body("User updated successfully");
    }
}
