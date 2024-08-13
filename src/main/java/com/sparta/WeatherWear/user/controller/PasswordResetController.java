package com.sparta.WeatherWear.user.controller;

import com.sparta.WeatherWear.user.dto.EmailRequestDTO;
import com.sparta.WeatherWear.user.dto.PasswordResetRequestDTO;
import com.sparta.WeatherWear.user.service.PasswordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/*
작성자 : 이승현
* 비밀번호 찾기를 위한 이메일 발송 및 처리
*/
@RestController
@RequiredArgsConstructor
@Tag(name = "비밀번호 찾기 API", description = "이메일 발송 및 처리")
@RequestMapping("/api/password")
public class PasswordResetController {

    private final PasswordService passwordService;

    /* 비밀번호를 잃어 버렸을 경우 메일 발송 */
    @PostMapping("/forgot")
    public ResponseEntity<String> requestPasswordReset(@RequestBody EmailRequestDTO requestDTO) {
        return passwordService.sendEmail(requestDTO);
    }
    /* 메일로 보내진 코드를 점검하고 비밀번호를 변경 */
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetRequestDTO requestDTO) {
        return passwordService.resetPassword(requestDTO);
    }
}
