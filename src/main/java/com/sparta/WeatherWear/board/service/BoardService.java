package com.sparta.WeatherWear.board.service;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardImage;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.board.repository.BoardImageRepository;
import com.sparta.WeatherWear.board.repository.BoardRepository;
import com.sparta.WeatherWear.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class BoardService {

    private final JwtUtil jwtUtil;
    private BoardRepository boardRepository;
    private BoardImageService boardImageService;

    @Transactional
    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> createBoard(BoardCreateRequestDto requestDto, @Valid List<MultipartFile> images) {
        // 예외처리
        if (requestDto == null) {
            throw new IllegalArgumentException("게시판 생성에 필요한 정보가 없습니다");
        }
        
        if (images == null) {
            throw new IllegalArgumentException("게시판 생성에 필요한 사진이 없습니다");
        }
        // request에서 받아온 값을 Board Entity로 만들기 
        Board newBoard = new Board(requestDto);

        // requestDto 확인
        System.out.println("requestDto.getUserId() = " + requestDto.getUserId());
        System.out.println("requestDto.getTitle() = " + requestDto.getTitle());
        System.out.println("requestDto.getContents() = " + requestDto.getContents());
        System.out.println("requestDto.isPrivate() = " + requestDto.isPrivate());
        System.out.println("requestDto.getStn() = " + requestDto.getStn());
        List<BoardTag> boardTags =  requestDto.getBoardTags();
        for (BoardTag boardTag : boardTags) {
            System.out.println("boardTag = " + boardTag);
        }

        // Board Entity -> db에 저장
        boardRepository.save(newBoard);

        // 추가 - 사진 저장 메서드 실행
        boardImageService.uploadImage(newBoard, images);

        // 사진 확인
        List<BoardImage> boardImages = newBoard.getBoardImages();
        for (BoardImage boardImage : boardImages) {
            System.out.println("boardImage_path = " + boardImage.getImagePath());
        }

        // 이때, 날씨 정보 & 가져와서 넣기 -> newBoard에
        int stn = requestDto.getStn();
        // stn 값 통해서 날씨 정보 가져오기

        // 날씨 정보 저장 -> 날씨 정보 db에 이미 있는지 검증 (캐싱)


        // newBoard -> responseDto로 반환
        BoardCreateResponseDto responseDto = new BoardCreateResponseDto(newBoard);
        // Creating the ApiResponse object
        ApiResponse<BoardCreateResponseDto> response = new ApiResponse<>(201, "Board created successfully", responseDto);
        // Returning the response entity with the appropriate HTTP status
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }



    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> findBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->
                new IllegalArgumentException("선택한 게시물은 없는 게시물입니다.")
        );

        // newBoard -> responseDto로 반환
        BoardCreateResponseDto responseDto = new BoardCreateResponseDto(board);
        // Creating the ApiResponse object
        ApiResponse<BoardCreateResponseDto> response = new ApiResponse<>(200, "Board responsed successfully", responseDto);
        // Returning the response entity with the appropriate HTTP status
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardByUserId(Long userId) {
        List<Board> boards = boardRepository.findByUserId(userId);

        // 예외처리 추가 필요
        if (boards.isEmpty()) {
            log.info("해당 유저가 없거나 유저가 작성한 게시물이 없습니다");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // newBoard -> responseDto로 반환
        List<BoardCreateResponseDto> responseDtos = new ArrayList<>();

        for (Board board : boards) {
            responseDtos.add(new BoardCreateResponseDto(board));
        }
        // Creating the ApiResponse object
        ApiResponse<List<BoardCreateResponseDto>> response = new ApiResponse<>(200, "Board responsed successfully", responseDtos);
        // Returning the response entity with the appropriate HTTP status
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardAll(Long user_id, BoardfindRequestDto requestDTO) {
//    }
//
//    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> updateBoard(Long userId, BoardUpdateRequestDto requestDTO) {
//    }
//
//    public ResponseEntity<String> removeBoard(Long userId) {
//    }

    public Claims getInfoFromToken(String tokenValue, HttpServletResponse res) {
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);
        // 토큰 검증
        if(!jwtUtil.validateToken(token, res)){
            throw new IllegalArgumentException("Token Error");
        }
        // 토큰에서 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        return info;
    }
}
