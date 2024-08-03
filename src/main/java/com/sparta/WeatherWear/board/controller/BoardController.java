package com.sparta.WeatherWear.board.controller;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.service.BoardService;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
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
    @PostMapping("/")
    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> createBoard(@RequestBody @Valid BoardCreateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestParam("images") List<MultipartFile> images) {
        return boardService.createBoard(requestDto, userDetails, images);

    }

    /* 게시물 id로 조회 */
    @GetMapping("/board-id/{boardId}")
    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> findBoardById(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.findBoardById(boardId, userDetails);
    }

    /* 게시물 user_id 전체 목록 조회 (페이징) */
    @GetMapping("/user-id/{userId}")
    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardByUserId(@PathVariable Long userId) {
        return boardService.findBoardByUserId(userId);
    }

    /* 게시물 전체 목록 조회 (페이징) & 아이디에 해당하는 값 있으면 수정 기능 추가하기 */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardAll(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.findBoardAll(userDetails);
    }

    /* 게시물 수정 */
    @PutMapping("/")
    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> updateBoard(@RequestBody @Valid BoardUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("images") List<MultipartFile> images) {
        return boardService.updateBoard(requestDto, userDetails, images);
    }

    /* 게시물 삭제 (게시물을 작성한 유저가 맞는지) */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> removeBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.removeBoard(boardId, userDetails);
    }

//    /* 게시물 이미지 전체 불러오기 */
//    @GetMapping("/images/{boardId}")
//    public ResponseEntity<String> userBoardImages(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return boardService.removeBoard(boardId, userDetails);
//    }
    
    /* 게시물 좋아요 변경 */
    @GetMapping("/likes/{boardId}")
    public ResponseEntity<String> switchBoardLikes(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.switchBoardLikes(boardId, userDetails);
    }

//    /* 특정 회원의 게시물 이미지 전체 불러오기 */
//    @GetMapping("/images/{user_id}")
//    public ResponseEntity<String> userBoardImagesById(@PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return boardService.removeBoard(board_id, userDetails);
//    }
//
//    /* 게시물 태그 전체 불러오기 */
//    @GetMapping("/tags/")
//    public ResponseEntity<String> userBoardTags(@PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return boardService.removeBoard(board_id, userDetails);
//    }

//    /* 특정 회원의 게시물 태그 전체 불러오기 */
//    @DeleteMapping("/tags/{user_id}")
//    public ResponseEntity<String> userBoardTagsById(@PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return boardService.removeBoard(board_id, userDetails);
//    }


}

