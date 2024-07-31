package com.sparta.WeatherWear.board.service;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardImage;
import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.board.repository.BoardImageRepository;
import com.sparta.WeatherWear.board.repository.BoardLikeRepository;
import com.sparta.WeatherWear.board.repository.BoardRepository;
import com.sparta.WeatherWear.board.repository.BoardTagRepository;
import com.sparta.WeatherWear.entity.User;
import com.sparta.WeatherWear.entity.Weather;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import com.sparta.WeatherWear.service.WeatherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BoardService {

    private WeatherService weatherService;
    private BoardTagRepository boardTagRepository;
    private BoardLikeRepository boardLikeRepository;
    private final BoardImageRepository boardImageRepository;
    private BoardRepository boardRepository;
    private BoardImageService boardImageService;

    @Transactional
    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> createBoard(BoardCreateRequestDto requestDto, Long id, UserDetailsImpl userDetails, @Valid List<MultipartFile> images) {
        // 예외처리
        if (requestDto == null) {
            throw new IllegalArgumentException("게시판 생성에 필요한 정보가 없습니다");
        }
        
        if (images == null) {
            throw new IllegalArgumentException("게시판 생성에 필요한 사진이 없습니다");
        }
        // user 정보 가져오기 (id)
        User user = userDetails.getUser();

        // 날씨 정보 저장 -> 날씨 정보 db에 이미 있는지 검증 (캐싱)
        Weather weather = weatherService.getWeatherByAddress(id);

        // request에서 받아온 값을 Board Entity로 만들기 
        Board newBoard = new Board(requestDto, user, weather); // Weather 추가하기

        // requestDto 확인
        System.out.println("userDetails.getUser().getId() = " + userDetails.getUser().getId());
        System.out.println("requestDto.getTitle() = " + requestDto.getTitle());
        System.out.println("requestDto.getContents() = " + requestDto.getContents());
        System.out.println("requestDto.isPrivate() = " + requestDto.isPrivate());
        System.out.println("requestDto.getStn() = " + requestDto.getStn());
        System.out.println("requestDto.getColor() = " + requestDto.getColor());
        System.out.println("requestDto.getType() = " + requestDto.getType());

        // Board Entity -> db에 저장
        boardRepository.save(newBoard);

        // 추가 - 사진 저장 메서드 실행
        boardImageService.uploadImage(newBoard, images);

        // 사진 확인
        List<BoardImage> boardImages = newBoard.getBoardImages();
        for (BoardImage boardImage : boardImages) {
            System.out.println("boardImage_path = " + boardImage.getImagePath());
        }
        
        // 추가 - 태그 저장 메서드 실행
        boardTagRepository.save(new BoardTag(newBoard, requestDto.getColor(), requestDto.getType()));

        // 추가 - 좋아요 저장 메서드 실행
        boardLikeRepository.save(new BoardLike(user,newBoard));

        // newBoard -> responseDto로 반환
        BoardCreateResponseDto responseDto = new BoardCreateResponseDto(newBoard, requestDto.getColor(), requestDto.getType());
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

    // 페이징 구현 추가 필요
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

    // 페이징 구현 추가 필요
    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardAll() {
        List<Board> boards = boardRepository.findAll();
        List<BoardCreateResponseDto> responseDtos = new ArrayList<>();

        for (Board board : boards) {
            responseDtos.add(new BoardCreateResponseDto(board));
        }
        // Creating the ApiResponse object
        ApiResponse<List<BoardCreateResponseDto>> response = new ApiResponse<>(200, "Board responsed successfully", responseDtos);
        // Returning the response entity with the appropriate HTTP status
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @Transactional
    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> updateBoard(BoardUpdateRequestDto requestDTO, UserDetailsImpl userDetails, List<MultipartFile> images) {
        if (requestDTO == null) {
            log.info("요청한 수정 내용이 없습니다.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // user 정보 가져오기 (id)
        Long userId = userDetails.getUser().getId();

        // 유저 아이디와 게시물의 id 가 같은지 확인
        Long boardUserId = requestDTO.getUserId();

        if(userId == null || boardUserId == null) {
            log.info("User의 Id 값이 없습니다.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // 같으면 update 실행
        if(boardUserId.equals(userId)) {
            // 수정할 board을 가져오기
            Board board = boardRepository.findById(requestDTO.getUserId()).orElseThrow(()->
                    new IllegalArgumentException("선택한 게시물은 없는 게시물입니다.")
            );;
            // request 로 받아 온 값 넣기
            Board updateBoard = board.update(requestDTO);
            
            // 날씨 정보 & 사진 업데이트하기
            // 사진 업데이트
            if(images != null) {
                // 기존 사진을 제거해야 한다
                List<BoardImage> boardImages = updateBoard.getBoardImages();
                boardImageRepository.deleteAll(boardImages);

                // 추가 - 사진 저장 메서드 실행
                boardImageService.uploadImage(updateBoard, images);

                // 사진 확인
                for (BoardImage boardImage : boardImages) {
                    System.out.println("boardImage_path = " + boardImage.getImagePath());
                }
            }

            // 해당하는 날씨 정보가 존재하는 지 먼저 확인 (캐싱)


            // newBoard -> responseDto로 반환
            BoardCreateResponseDto responseDto = new BoardCreateResponseDto(updateBoard);
            // Creating the ApiResponse object
            ApiResponse<BoardCreateResponseDto> response = new ApiResponse<>(200, "Board updated successfully", responseDto);
            // Returning the response entity with the appropriate HTTP status
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        }else {
            log.info("User의 Id 값이 없습니다.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        
    }
    public ResponseEntity<String> removeBoard(Long boardId, UserDetailsImpl userDetails) {

        Board board = boardRepository.findById(boardId).orElseThrow(()->
                new IllegalArgumentException("선택한 게시물을 찾을 수 없습니다.")
        );

        // 사용자가 작성한 게시물인지 확인
        Long userId = userDetails.getUser().getId();
        Long boardUserId = board.getUser().getId();

        if(userId == null || boardUserId == null) {
            log.info("User의 Id 값이 없습니다.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 삭제
        if(boardUserId.equals(userId)) {
            boardRepository.delete(board);
        }

        return new ResponseEntity<>(HttpStatus.OK);


    }

}
