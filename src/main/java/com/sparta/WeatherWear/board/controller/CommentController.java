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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class CommentController {

    private final CommentService commentService;

    /* 댓글 생성 */
    @PostMapping("/comments/{board_id}")
    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> addBoardComments(@RequestBody @Valid CommentCreateRequestDto requestDto, @PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addComments(requestDto, board_id, userDetails);
    }
}
