package com.sparta.WeatherWear.user.service;

import com.sparta.WeatherWear.global.service.EmailService;
import com.sparta.WeatherWear.global.service.RedisService;
import com.sparta.WeatherWear.user.dto.EmailRequestDTO;
import com.sparta.WeatherWear.user.dto.PasswordResetRequestDTO;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<String> sendEmail(EmailRequestDTO requestDTO) {
        String email = requestDTO.getEmail();
        if(userRepository.existsByEmail(email)){
            String resetCode = UUID.randomUUID().toString(); // 임의의 코드 생성
            if(emailService.sendPasswordResetEmail(email, resetCode)){ // 메일 발송
                redisService.save(RedisService.RESET_CODE_PREFIX,resetCode,email,RedisService.RESET_CODE_DURATION); // Redis에 저장
                return ResponseEntity.ok("메일 전송이 완료되었습니다.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가입되지 않은 메일입니다.");
    }

    @Transactional
    public ResponseEntity<String> resetPassword(PasswordResetRequestDTO requestDTO) {
        if(!requestDTO.getNewPassword().equals(requestDTO.getNewPasswordCheck()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("비밀번호가 일치하지 않습니다.");
        String email = redisService.get(RedisService.RESET_CODE_PREFIX,requestDTO.getCode());
        if(email== null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("올바르지 않은 코드값입니다.");
        }else{
            User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
            user.updatePassword(passwordEncoder.encode(requestDTO.getNewPassword()));
            redisService.delete(RedisService.RESET_CODE_PREFIX,requestDTO.getCode());
            return ResponseEntity.ok("비밀번호 변경이 완료되었습니다.");
        }
    }
}
