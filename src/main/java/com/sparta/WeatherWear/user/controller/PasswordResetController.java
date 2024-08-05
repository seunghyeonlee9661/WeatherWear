package com.sparta.WeatherWear.user.controller;

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
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        return passwordService.sendEmail(email);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetRequestDTO requestDTO) {
        return passwordService.resetPassword(requestDTO);
    }
}
