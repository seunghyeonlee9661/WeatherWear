package com.sparta.WeatherWear.board.controller;

import com.sparta.WeatherWear.board.dto.ApiResponse;
import com.sparta.WeatherWear.board.dto.CommentCreateRequestDto;
import com.sparta.WeatherWear.board.dto.CommentCreateResponseDto;
import com.sparta.WeatherWear.board.dto.CommentUpdateRequesteDto;
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
@RequestMapping("/api/board/comments")
public class CommentController {

    private final CommentService commentService;

    /* 댓글 생성 */
    @PostMapping("/{boardId}")
    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> addBoardComments(@RequestBody @Valid CommentCreateRequestDto requestDto, @PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addComments(requestDto, boardId, userDetails);
    }    
    /* BoardId에 해당하는 댓글 모두 조회 */
    @GetMapping("/{boardId}")
    public List<CommentCreateResponseDto> findBoardCommentsByBoardId(@PathVariable Long boardId) {
        return commentService.findBoardCommentsByBoardId(boardId);
    }    
//    /* User가 작성한 댓글 모두 조회  */
//    @PostMapping("/comments/{boardId}")
//    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> addBoardComments(@RequestBody @Valid CommentCreateRequestDto requestDto, @PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return commentService.addComments(requestDto, boardId, userDetails);
//    }
    /* 댓글 수정 */
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> updateBoardComment(@RequestBody @Valid CommentUpdateRequesteDto requestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateBoardComment(requestDto, commentId, userDetails);
    }
    /* 댓글 삭제 */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteBoardComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteBoardComment(commentId, userDetails);
    }    
    
//    /* 댓글 좋아요 변경 */
//    @DeleteMapping("/comments/{boardId}")
//    public ResponseEntity<String> deleteBoardComment(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return commentService.deleteBoardComment(boardId, userDetails);
//    }
}
