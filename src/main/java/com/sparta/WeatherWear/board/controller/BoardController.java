package com.sparta.WeatherWear.board.controller;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.service.BoardService;
import com.sparta.WeatherWear.security.JwtUtil;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    /* 게시물 작성 */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> createBoard(@RequestBody BoardCreateRequestDto requestDto, @Valid @RequestParam("images") List<MultipartFile> images) {
        return boardService.createBoard(requestDto, images);
    }

    /* 게시물 id로 조회 */
    @GetMapping("/find/{board_id}")
    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> findBoardById(@PathVariable Long board_id) {
        return boardService.findBoardById(board_id);
    }

    /* 게시물 user_id 전체 목록 조회 (페이징) */
    @GetMapping("/find/{user_id}")
    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardByUserId(@PathVariable Long user_id) {
        return boardService.findBoardByUserId(user_id);
    }

    /* 게시물 전체 목록 조회 (페이징) & 아이디에 해당하는 값 있으면 수정 기능 추가하기 */
    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardAll(@RequestBody BoardfindRequestDto requestDto) {
        return boardService.findBoardAll(requestDto);
    }

    /* 게시물 수정 */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> updateBoard(@RequestBody BoardUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("images") List<MultipartFile> images) {
        return boardService.updateBoard(requestDto, userDetails, images);
    }

//    /* 게시물 삭제 (게시물을 작성한 유저가 맞는지) */
//    @DeleteMapping("/{user_id}")
//    public ResponseEntity<String> removeBoard(@PathVariable Long user_id) {
//        return boardService.removeBoard(user_id);
//    }
}
