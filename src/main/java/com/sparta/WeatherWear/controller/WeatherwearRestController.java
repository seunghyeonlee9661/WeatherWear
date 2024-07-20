package com.sparta.WeatherWear.controller;

import com.sparta.WeatherWear.dto.UserRequestDTO;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import com.sparta.WeatherWear.service.WeatherwearService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class WeatherwearRestController {
    private final WeatherwearService service;

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
}
