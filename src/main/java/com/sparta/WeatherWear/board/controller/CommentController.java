package com.sparta.WeatherWear.board.controller;

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

/*
  작성자 : 하준영
 */
@RequiredArgsConstructor
@RestController
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
    public List<CommentCreateResponseDto> findBoardCommentsByBoardId(@PathVariable Long boardId, @RequestParam Long page) {
        return commentService.findBoardCommentsByBoardId(boardId,page);
    }    
//    /* User가 작성한 댓글 모두 조회  */
//    @PostMapping("/comments/{boardId}")
//    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> addBoardComments(@RequestBody @Valid CommentCreateRequestDto requestDto, @PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return commentService.addComments(requestDto, boardId, userDetails);
//    }
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
    
//    /* 댓글 좋아요 변경 */
    @GetMapping("/comments/likes/{commentId}")
    public ResponseEntity<?> switchCommentLikes(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.switchCommentLikes(commentId, userDetails);
    }
}
