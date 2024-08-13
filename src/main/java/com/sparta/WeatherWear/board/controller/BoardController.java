package com.sparta.WeatherWear.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.service.BoardService;
import com.sparta.WeatherWear.global.security.JwtUtil;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

  /*
    작성자 : 하준영
   */

@RequiredArgsConstructor
@RestController
@Tag(name = "게시판 API", description = "게시판 관련 API")
@RequestMapping("/api/boards")
public class BoardController {

    private static final Logger log = LoggerFactory.getLogger(BoardController.class);
    @Autowired
    private final BoardService boardService;

    /* 게시물 작성 */
    @PostMapping("")
    public ResponseEntity<?> createBoard(@RequestPart("data") String data, @RequestPart(value = "file") MultipartFile image, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        BoardCreateRequestDto boardCreateRequestDto = new ObjectMapper().readValue(data, BoardCreateRequestDto.class); // json 형식을 DTO로 파싱합니다.
        return boardService.createBoard(boardCreateRequestDto,userDetails, image);
    }

    /* 상세 조회 : 게시물 id로 조회 */
    @GetMapping("/{boardId}")
    public ResponseEntity<?> findBoardById(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        return boardService.findBoardById(boardId, userDetails,request);
    }


    /* 게시물 전체 목록 조회 -> ootd 트렌드 페이지 */
    @GetMapping("")
    public ResponseEntity<List<BoardCreateResponseDto>> findBoardAll(@RequestParam(required = false) Long lastId, @RequestParam(required = false) String color, @RequestParam(required = false) String type, @RequestParam(required = false) String keyword) {
        return boardService.findBoardAll(lastId, color, type, keyword);
    }


    /* 게시물 수정 */
    @PutMapping("")
    public ResponseEntity<?> updateBoard( @RequestPart("data") String data, @RequestPart(value = "file", required = false) MultipartFile image, @AuthenticationPrincipal UserDetailsImpl userDetails  ) throws IOException {
        BoardUpdateRequestDto requestDto = new ObjectMapper().readValue(data, BoardUpdateRequestDto.class); // json 형식을 DTO로 파싱합니다.
        return boardService.updateBoard(requestDto, userDetails, image);
    }

    /* 게시물 삭제 (게시물을 작성한 유저가 맞는지) */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> removeBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.removeBoard(boardId, userDetails);
    }

    /* 게시물 좋아요 변경 */
    @PostMapping("/likes/{boardId}")
    public ResponseEntity<?> switchBoardLikes(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.switchBoardLikes(boardId, userDetails);
    }
}

