package com.sparta.WeatherWear.board.service;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.Comment;
import com.sparta.WeatherWear.board.repository.BoardRepository;
import com.sparta.WeatherWear.board.repository.CommentRepository;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.user.entity.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  작성자 : 하준영
 */
@Service
@AllArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository  boardRepository;

    /* 댓글 생성 */
    public ResponseEntity<?> addComments(@Valid CommentCreateRequestDto requestDto, Long boardId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Board board = boardRepository.findById(boardId).orElseThrow(()->
                new IllegalArgumentException("게시물을 찾을 수 없습니다")
        );

        if(requestDto.getContents().isEmpty()){
            throw new IllegalArgumentException("수정할 내용이 없습니다");
        }

        Comment newComment = new Comment(user,board,requestDto.getContents());
        commentRepository.save(newComment);
        board.addComment(newComment);

        // newBoard -> responseDto로 반환
        return new ResponseEntity<>("댓글 생성 성공", HttpStatus.CREATED);
    }

    /* BoardId에 해당하는 댓글 모두 조회 */
    public List<CommentCreateResponseDto> findBoardCommentsByBoardId(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new IllegalArgumentException("게시물을 찾을 수 없습니다")
        );

        // Get the current page content
        List<Comment> comments = commentRepository.findAllByBoardIdOrderedByCreatedAt(boardId);

        List<CommentCreateResponseDto> commentCreateResponseDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentCreateResponseDtos.add(new CommentCreateResponseDto(comment));
        }
        return commentCreateResponseDtos;
    }
    /* 댓글 수정 */
    public ResponseEntity<?> updateBoardComment(CommentUpdateRequesteDto requestDto, Long commentId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new IllegalArgumentException("댓글을 찾을 수 없습니다")
        );

        // 업데이트
        if(user.getId().equals(comment.getUser().getId())) {
            Comment updatedComment = comment.update(requestDto.getContents());
            commentRepository.save(updatedComment);

            // Returning the response entity with the appropriate HTTP status
            return new ResponseEntity<>("수정 완료", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("수정 권한이 없습니다.",HttpStatus.NO_CONTENT);
        }

    }
    /* 댓글 삭제 */
    public ResponseEntity<String> deleteBoardComment(Long commentId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new IllegalArgumentException("댓글을 찾을 수 없습니다")
        );

        if(user.getId().equals(comment.getUser().getId())) {
            commentRepository.deleteById(commentId);
        }
        return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
    }
}
