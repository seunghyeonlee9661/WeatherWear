package com.sparta.WeatherWear.user.controller;

import com.sparta.WeatherWear.user.dto.EmailRequestDTO;
import com.sparta.WeatherWear.user.dto.PasswordResetRequestDTO;
import com.sparta.WeatherWear.user.service.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/password")
public class PasswordResetController {

    private final PasswordService passwordService;

    @PostMapping("/forgot")
    public ResponseEntity<String> requestPasswordReset(@RequestBody EmailRequestDTO requestDTO) {
        return passwordService.sendEmail(requestDTO);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetRequestDTO requestDTO) {
        return passwordService.resetPassword(requestDTO);
    }
}
