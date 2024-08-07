package com.sparta.WeatherWear.board.controller;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.service.BoardService;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/*
* 게시물 CRUD와 좋아요 기능 구현
*/
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    // 게시물 추가 기능 : MultipartFile을 위한 form-data 때문에 각각 받아옴 -> 이후에 개선 필요
    @PostMapping("/boards")
    public ResponseEntity<String> createBoard(
            @RequestPart("address") @NotBlank(message = "주소값이 없습니다.") String address,
            @RequestPart("address_id") @NotNull(message = "행정동 코드값이 없습니다.") Long addressId,
            @RequestPart("title") @NotBlank(message = "제목이 없습니다.") String title,
            @RequestPart("contents") @NotBlank(message = "내용이 없습니다.") String contents,
            @RequestPart("isPrivate") boolean isPrivate,
            @RequestPart("tags") List<BoardTagDTO> tags,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        BoardCreateRequestDto requestDto = new BoardCreateRequestDto(address,addressId,title,contents,isPrivate,tags);
        return boardService.createBoard(requestDto, userDetails, file);
    }

    /* 게시물 상세 정보 */
    @GetMapping("/boards/{id}")
    public ResponseEntity<?> findBoardById(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        return boardService.findBoardById(id, userDetails,request);
    }

    /* 게시물 전체 조회 : 커서 기반 페이지네이션, 검색어를 통한 검색 기능 */
    @GetMapping("/boards")
    public ResponseEntity<List<BoardListResponseDTO>> findBoardList(@RequestParam(value = "lastId", required = false) Long lastId,@RequestParam(value = "search", required = false, defaultValue = "") String search) {
        return boardService.findBoardList(lastId,search);
    }

    // 게시물 추가 기능 : MultipartFile을 위한 form-data 때문에 각각 받아옴 -> 이후에 개선 필요
    @PutMapping("/boards")
    public ResponseEntity<String> updateBoard(
            @RequestPart("id") long id,
            @RequestPart("title") @NotBlank(message = "제목이 없습니다.") String title,
            @RequestPart("contents") @NotBlank(message = "내용이 없습니다.") String contents,
            @RequestPart("isPrivate") boolean isPrivate,
            @RequestPart("tags") List<BoardTagDTO> tags,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        BoardUpdateRequestDto requestDto = new BoardUpdateRequestDto(id,title,contents,isPrivate,tags);
        return boardService.updateBoard(requestDto, userDetails, file);
    }

    /* 게시물 삭제 */
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<String> removeBoard(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return boardService.removeBoard(id, userDetails);
    }

    /* 게시물 좋아요 설정 */
    @PutMapping("/boards/{id}/like")
    public ResponseEntity<String> setLike(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return boardService.setLike(id, userDetails);
    }
}

