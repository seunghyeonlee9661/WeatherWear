package com.sparta.WeatherWear.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.service.BoardService;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public ResponseEntity<?> createBoard(@RequestPart("data") String data,
                                         @RequestPart(value = "file") MultipartFile image,
//                                             @NotBlank(message = "주소값이 없습니다.") String address,
//                                         @RequestPart("addressId") @NotNull(message = "행정동 코드값이 없습니다.") String addressId,
//                                         @RequestPart("title") @NotBlank(message = "제목이 없습니다.") String title,
//                                         @RequestPart("contents") @NotBlank(message = "내용이 없습니다.") String contents,
//                                         @RequestPart("isPrivate") boolean isPrivate,
//                                         @RequestPart("tags") List<ClothesRequestDTO> tags,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                         HttpServletRequest request) throws IOException {
        // Log raw request body for debugging
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        System.out.println("Raw Request Body: " + requestBody.toString());
        System.out.println("Data Part: " + data);

        // You can also manually parse JSON if needed
        ObjectMapper objectMapper = new ObjectMapper();
        BoardCreateRequestDto boardCreateRequestDto = objectMapper.readValue(data, BoardCreateRequestDto.class);
        System.out.println("Parsed DTO: " + boardCreateRequestDto);

//        BoardCreateRequestDto requestDto = new BoardCreateRequestDto(address,Long.valueOf(addressId),title,contents,isPrivate,tags);
//        System.out.println(requestDto.getAddress());
//        System.out.println(requestDto.getAddressId());
//        System.out.println(requestDto.getTags());
//        System.out.println(requestDto.getContents());
//        System.out.println(requestDto.getTitle());
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
                                                                     @RequestParam(required = false) Long addressId,
                                                                     @RequestParam(required = false) Integer sky,
                                                                     @RequestParam(required = false) String color,
                                                                     @RequestParam(required = false) String type) {
        return boardService.findBoardAll(userDetails, lastId,addressId, sky, color, type);
    }


    /* 게시물 수정 */
//    @PutMapping("/")
//    public ResponseEntity<?> updateBoard(@Validated @RequestPart(value = "boardUpdateRequestDto") BoardUpdateRequestDto requestDto,
//                                                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
//                                                                           @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
//        return boardService.updateBoard(requestDto, userDetails, image);
//    }
    @PutMapping("/")
    public ResponseEntity<?> updateBoard( @RequestPart("boardId") @NotNull(message = "boardId가 없습니다.") Long boardId,
                                          @RequestPart("address") @NotBlank(message = "주소값이 없습니다.") String address,
                                          @RequestPart("addressId") @NotNull(message = "행정동 코드값이 없습니다.") Long addressId,
                                          @RequestPart("title") @NotBlank(message = "제목이 없습니다.") String title,
                                          @RequestPart("contents") @NotBlank(message = "내용이 없습니다.") String contents,
                                          @RequestPart("isPrivate") boolean isPrivate,
                                          @RequestPart("tags") List<ClothesRequestDTO> tags,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        BoardUpdateRequestDto requestDto = new BoardUpdateRequestDto(boardId,address,addressId ,title,contents,isPrivate,tags);
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

