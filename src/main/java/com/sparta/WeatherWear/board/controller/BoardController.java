package com.sparta.WeatherWear.board.controller;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.service.BoardService;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<?> createBoard(@Validated @RequestPart(value = "boardCreateRequestDto")  BoardCreateRequestDto requestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestPart(value = "image") MultipartFile image) throws IOException {
        return boardService.createBoard(requestDto,userDetails, image);

    }

    /*
        상세 조회
        게시물 id로 조회 
    */
    @GetMapping("/{boardId}")
    public ResponseEntity<?> findBoardById(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        return boardService.findBoardById(boardId, userDetails,request);
    }

//    /* 게시물 user_id 전체 목록 조회 (페이징) */
//    @GetMapping("/by-user-id/{userId}")
//    public ResponseEntity<List<BoardCreateResponseDto>> findBoardByUserId(@PathVariable Long userId) {
//        return boardService.findBoardByUserId(userId);
//    }

    /*
        <MainPage>
        게시물 전체 목록 조회 -> ootd 트렌드 페이지
    */
    @GetMapping("")
    public ResponseEntity<List<BoardCreateResponseDto>> findBoardAll(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                     @RequestParam(required = false) Long lastId,
                                                                     @RequestParam(required = false) Long addressId,
                                                                     @RequestParam(required = false) Integer sky,
                                                                     @RequestParam(required = false) String color,
                                                                     @RequestParam(required = false) String type) {
        return boardService.findBoardAll(userDetails, lastId,addressId, sky, color, type);
    }


    /*
        게시물 전체 목록 조회
        &검색 필터링
        검색 필터링 (도시, 날씨 , 옷 타입, 옷 컬러)
    */
//    // 도시 검색
//    @GetMapping("/search")
//    public ResponseEntity<?> findBoardAllByCity(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String city, @RequestParam Long page) {
//        return boardService.findBoardAllByCity(userDetails, city, page);
//    }
//
//    // 날씨 검색
//    @GetMapping("/search")
//    public ResponseEntity<?> findBoardAllByWeather(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestParam Long weather, @RequestParam Long page) {
//        return boardService.findBoardAllByWeather(userDetails, weather, page);
//    }
//
//    // 옷 컬러 검색
//    @GetMapping("/search")
//    public ResponseEntity<?> findBoardAllByColor(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam ClothesColor color, @RequestParam Long page) {
//        return boardService.findBoardAllByColor(userDetails, color, page);
//    }
//
//    // 옷 타입 검색
//    @GetMapping("/search")
//    public ResponseEntity<?> findBoardAllByType(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam ClothesType type, @RequestParam Long page) {
//        return boardService.findBoardAllByType(userDetails, type, page);
//    }

    /* 게시물 수정 */
    @PutMapping("/")
    public ResponseEntity<?> updateBoard(@Validated @RequestPart(value = "boardUpdateRequestDto") BoardUpdateRequestDto requestDto,
                                                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                           @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
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


//    /* 게시물 이미지 전체 불러오기 */
//    @GetMapping("/images/{boardId}")
//    public ResponseEntity<String> userBoardImages(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return boardService.removeBoard(boardId, userDetails);
//    }


//    /* 특정 회원의 게시물 이미지 전체 불러오기 */
//    @GetMapping("/images/{user_id}")
//    public ResponseEntity<String> userBoardImagesById(@PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return boardService.removeBoard(board_id, userDetails);
//    }
//
//    /* 게시물 태그 전체 불러오기 */
//    @GetMapping("/tags/")
//    public ResponseEntity<String> userBoardTags(@PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return boardService.removeBoard(board_id, userDetails);
//    }

//    /* 특정 회원의 게시물 태그 전체 불러오기 */
//    @DeleteMapping("/tags/{user_id}")
//    public ResponseEntity<String> userBoardTagsById(@PathVariable Long board_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return boardService.removeBoard(board_id, userDetails);
//    }


}

