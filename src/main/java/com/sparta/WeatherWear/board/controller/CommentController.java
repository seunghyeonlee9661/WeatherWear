package com.sparta.WeatherWear.board.controller;

import com.sparta.WeatherWear.board.dto.ApiResponse;
import com.sparta.WeatherWear.board.dto.CommentCreateRequestDto;
import com.sparta.WeatherWear.board.dto.CommentCreateResponseDto;
import com.sparta.WeatherWear.board.service.CommentService;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class CommentController {

    private final CommentService commentService;

    /* 댓글 생성 */
    @PostMapping("/comments/{boardId}")
    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> addBoardComments(@RequestBody @Valid CommentCreateRequestDto requestDto, @PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addComments(requestDto, boardId, userDetails);
    }    
    /* BoardId에 해당하는 댓글 모두 조회 */
    @GetMapping("/comments/{boardId}")
    public ResponseEntity<ApiResponse<List<CommentCreateResponseDto>>> findBoardCommentsByBoardId(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.findBoardCommentsByBoardId(boardId, userDetails);
    }    
//    /* User가 작성한 댓글 모두 조회  */
//    @PostMapping("/comments/{boardId}")
//    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> addBoardComments(@RequestBody @Valid CommentCreateRequestDto requestDto, @PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return commentService.addComments(requestDto, boardId, userDetails);
//    }
//    /* 댓글 수정 */
//    @PostMapping("/comments/{boardId}")
//    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> addBoardComments(@RequestBody @Valid CommentCreateRequestDto requestDto, @PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return commentService.addComments(requestDto, boardId, userDetails);
//    }
//    /* 댓글 삭제 */
//    @PostMapping("/comments/{boardId}")
//    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> addBoardComments(@RequestBody @Valid CommentCreateRequestDto requestDto, @PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return commentService.addComments(requestDto, boardId, userDetails);
//    }
}
