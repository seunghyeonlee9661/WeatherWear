package com.sparta.WeatherWear.board.controller;

import com.sparta.WeatherWear.dto.UserRequestDTO;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import com.sparta.WeatherWear.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    /* 게시물 작성 */
    @PostMapping("/")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRequestDTO requestDTO) {
        return service.createUser(requestDTO);
    }

    /* 게시물 id로 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<String>  removeUser(@RequestBody @Valid UserRequestDTO requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return service.updateUser(userDetails,requestDTO);
    }

    /* 게시물 전체 목록 조회 (페이징) */
    @GetMapping("/")
    public ResponseEntity<String>  removeUser(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return service.removeUser(userDetails,request.get("password"));
    }

    /* 게시물 수정 */
    @PutMapping("/{id}")
    public ResponseEntity<String>  removeUser(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return service.removeUser(userDetails,request.get("password"));
    }    
    
    /* 게시물 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<String>  removeUser(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return service.removeUser(userDetails,request.get("password"));
    }
}
