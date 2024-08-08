package com.sparta.WeatherWear.board.service;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.board.entity.Comment;
import com.sparta.WeatherWear.board.entity.CommentLike;
import com.sparta.WeatherWear.board.repository.BoardRepository;
import com.sparta.WeatherWear.board.repository.CommentLikeRepository;
import com.sparta.WeatherWear.board.repository.CommentRepository;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.user.entity.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final CommentLikeRepository commentLikeRepository;
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
//        CommentCreateResponseDto responseDto = new CommentCreateResponseDto(newComment);
        return new ResponseEntity<>("댓글 생성 성공", HttpStatus.CREATED);
    }

    /* BoardId에 해당하는 댓글 모두 조회 */
    public List<CommentCreateResponseDto> findBoardCommentsByBoardId(Long boardId, Long page) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new IllegalArgumentException("게시물을 찾을 수 없습니다")
        );

        // Define the page size (e.g., 8 items per page)
        int pageSize = 8;

        // Create a Pageable object
        Pageable pageable = PageRequest.of(page.intValue(), pageSize);

        // Retrieve the paginated results
        Page<Comment> commentPage = commentRepository.findAllOrderedByCreatedAt(pageable);

        // Get the current page content
        List<Comment> comments = commentPage.getContent();

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

            // newBoard -> responseDto로 반환
            CommentCreateResponseDto responseDto = new CommentCreateResponseDto(updatedComment);
            // Creating the ApiResponse object
//            ApiResponse<CommentCreateResponseDto> response = new ApiResponse<>(200, "comment updated successfully", responseDto);
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

    public ResponseEntity<?> switchCommentLikes(Long commentId, UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new IllegalArgumentException("댓글을 찾을 수 없습니다")
        );

        // 좋아요가 아예 없는 경우
        CommentLike newCommentLike = new CommentLike(user, comment);
        if(comment.getCommentLikes().isEmpty()) {
            comment.getCommentLikes().add(newCommentLike);
            commentLikeRepository.save(newCommentLike);
            int commentLikes = comment.getCommentLikes().size();
            return new ResponseEntity<>(commentLikes, HttpStatus.OK);
        }
        for(CommentLike commentLike : comment.getCommentLikes()) {
            System.out.println("commentLike = " + commentLike);
            System.out.println("commentLike.getUser() = " + commentLike.getUser());
            System.out.println("commentLike.getComment() = " + commentLike.getComment());

        }
        // 유저가 이미 좋아요 눌렀는 지 확인 & 성능 개선 필요
        CommentLike existingLike = commentLikeRepository.findByUserAndComment(user, comment);

        if (existingLike != null) {
            // User has already liked the post; remove the like
            comment.getCommentLikes().remove(existingLike);
            commentLikeRepository.delete(existingLike);
        } else {
            // User has not liked the post; add the like
            CommentLike newCommentLike2 = new CommentLike(user, comment);
            comment.getCommentLikes().add(newCommentLike2);
            commentLikeRepository.save(newCommentLike2);
        }
        int commentLikes = comment.getCommentLikes().size();

        // Prepare the response
        Map<String, Integer> response = new HashMap<>();
        response.put("commentLikes", commentLikes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
