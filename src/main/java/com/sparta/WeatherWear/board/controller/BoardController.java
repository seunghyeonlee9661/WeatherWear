package com.sparta.WeatherWear.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.service.BoardService;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

  /*
    작성자 : 하준영
   */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private final BoardService boardService;

    /* 게시물 작성 */
    @PostMapping("")
    public ResponseEntity<?> createBoard(@RequestPart("data") String data, // data 스트링
                                         @RequestPart(value = "file") MultipartFile image, // 파일 정보
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        BoardCreateRequestDto boardCreateRequestDto = new ObjectMapper().readValue(data, BoardCreateRequestDto.class); // json 형식을 DTO로 파싱합니다.
        return boardService.createBoard(boardCreateRequestDto,userDetails, image);
    }

    /*
        상세 조회
        게시물 id로 조회 
    */
    @GetMapping("/{boardId}")
    public ResponseEntity<?> findBoardById(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        return boardService.findBoardById(boardId, userDetails,request);
    }


    /*
        <MainPage>
        게시물 전체 목록 조회 -> ootd 트렌드 페이지
    */
    @GetMapping("")
    public ResponseEntity<List<BoardCreateResponseDto>> findBoardAll(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @RequestParam(required = false) Long lastId,
                                                                     @RequestParam(required = false) String address,
                                                                     @RequestParam(required = false) String color,
                                                                     @RequestParam(required = false) String type,
                                                                     @RequestParam(required = false) String keyword
                                                                     ) {
        return boardService.findBoardAll(userDetails, lastId, address, color, type, keyword);
    }


    /* 게시물 수정 */
//    @PutMapping("/")
//    public ResponseEntity<?> updateBoard(@Validated @RequestPart(value = "boardUpdateRequestDto") BoardUpdateRequestDto requestDto,
//                                                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                                           @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
//        return boardService.updateBoard(requestDto, userDetails, image);
//    }
    @PutMapping("")
    public ResponseEntity<?> updateBoard( @RequestPart("data") String data, // data 스트링
                                          @RequestPart(value = "file", required = false) MultipartFile image, // 파일 정보
//                                          @RequestPart("boardId") @NotNull(message = "boardId가 없습니다.") Long boardId,
//                                          @RequestPart("address") @NotBlank(message = "주소값이 없습니다.") String address,
//                                          @RequestPart("addressId") @NotNull(message = "행정동 코드값이 없습니다.") Long addressId,
//                                          @RequestPart("title") @NotBlank(message = "제목이 없습니다.") String title,
//                                          @RequestPart("contents") @NotBlank(message = "내용이 없습니다.") String contents,
//                                          @RequestPart("isPrivate") boolean isPrivate,
//                                          @RequestPart("tags") List<ClothesRequestDTO> tags,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails  ) throws IOException {
        BoardUpdateRequestDto requestDto = new ObjectMapper().readValue(data, BoardUpdateRequestDto.class); // json 형식을 DTO로 파싱합니다.
        return boardService.updateBoard(requestDto, userDetails, image);
    }

    /* 게시물 삭제 (게시물을 작성한 유저가 맞는지) */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> removeBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.removeBoard(boardId, userDetails);
    }

    /* 게시물 좋아요 변경 */
    @GetMapping("/likes/{boardId}")
    public ResponseEntity<?> switchBoardLikes(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.switchBoardLikes(boardId, userDetails);
    }



}

