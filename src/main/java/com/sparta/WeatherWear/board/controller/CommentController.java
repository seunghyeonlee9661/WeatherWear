package com.sparta.WeatherWear.board.controller;

import com.sparta.WeatherWear.board.dto.CommentCreateRequestDto;
import com.sparta.WeatherWear.board.dto.CommentCreateResponseDto;
import com.sparta.WeatherWear.board.dto.CommentUpdateRequesteDto;
import com.sparta.WeatherWear.board.service.CommentService;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
  작성자 : 하준영
 */
@RequiredArgsConstructor
@RestController
@Tag(name = "댓글 API", description = "댓글 관련 API")
@RequestMapping("/api/boards/")
public class CommentController {

    private final CommentService commentService;

    /* 댓글 생성 */
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<?> addBoardComments(@RequestBody @Valid CommentCreateRequestDto requestDto, @PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addComments(requestDto, boardId, userDetails);
    }    
    /* BoardId에 해당하는 댓글 모두 조회 */
    @GetMapping("/{boardId}/comments")
    public List<CommentCreateResponseDto> findBoardCommentsByBoardId(@PathVariable Long boardId) {
        return commentService.findBoardCommentsByBoardId(boardId);
    }    

    /* 댓글 수정 */
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateBoardComment(@RequestBody @Valid CommentUpdateRequesteDto requestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateBoardComment(requestDto, commentId, userDetails);
    }
    /* 댓글 삭제 */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteBoardComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteBoardComment(commentId, userDetails);
    }
}
