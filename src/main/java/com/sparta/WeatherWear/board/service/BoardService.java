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
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.weather.entity.Weather;
import com.sparta.WeatherWear.weather.service.WeatherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
  작성자 : 하준영
 */
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

    /* 게시물 작성 */
    @Transactional
    public ResponseEntity<BoardCreateResponseDto> createBoard(BoardCreateRequestDto requestDto, UserDetailsImpl userDetails, @Valid MultipartFile image) throws IOException {

        // user 정보 가져오기 (id)
        User user = userDetails.getUser();

        // 날씨 정보 저장 -> 날씨 정보 db에 이미 있는지 검증 (캐싱)
        // 법정동 코드 띄어쓰기 제거 필요
        Weather weather = weatherService.getWeatherByAddress(requestDto.getAddressId());

        //초기 조회수 0으로 설정
//        if(requestDto.getViews() == 0){
//
//        }

        // request에서 받아온 값을 Board Entity로 만들기
        Board newBoard = new Board(requestDto, user, weather); // Weather 추가하기

        // requestDto 확인
        System.out.println("userDetails.getUser().getId() = " + userDetails.getUser().getId());
        System.out.println("requestDto.getTitle() = " + requestDto.getTitle());
        System.out.println("requestDto.getContents() = " + requestDto.getContents());
        System.out.println("requestDto.isPrivate() = " + requestDto.isPrivate());
        System.out.println("requestDto.getAddressId() = " + requestDto.getAddressId());
//        System.out.println("requestDto.getViews() = " + requestDto.getViews());


        // Board Entity -> db에 저장
        boardRepository.save(newBoard);

        // 추가 - 태그 저장 메서드 실행
        for (ClothesRequestDTO clothesRequestDTO: requestDto.getClothesRequestDTO()) {
            System.out.println("clothesRequestDTO.getColor() = " + clothesRequestDTO.getColor());
            System.out.println("clothesRequestDTO.getType() = " + clothesRequestDTO.getType());
            BoardTag newBoardTag = new BoardTag(newBoard, clothesRequestDTO.getColor(), clothesRequestDTO.getType());
            boardTagRepository.save(newBoardTag);
            // Board Entity에 추가
            newBoard.getBoardTags().add(newBoardTag);
        }
        // 추가 - 사진 저장 메서드 실행
        BoardImage boardImagePath = boardImageService.uploadImage(newBoard, image);
        // Board Entity에 추가
        newBoard.getBoardImages().add(boardImagePath);

        // 사진 확인
        System.out.println("boardImage_path = " + boardImagePath);

        // 추가 - 좋아요 저장 메서드 실행
//        BoardLike newBoardLike = new BoardLike(user,newBoard);
//        boardLikeRepository.save(newBoardLike);
        // Board Entity에 추가
//        newBoard.getBoardLikes().add(newBoardLike);

//        Board updateImageToBoard = newBoard.update(boardImagePath);
        // newBoard -> responseDto로 반환
        BoardCreateResponseDto responseDto = new BoardCreateResponseDto(newBoard, requestDto.getClothesRequestDTO());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

    }


    /* 게시물 id로 조회 */
    public ResponseEntity<?> findBoardById(Long boardId, UserDetailsImpl userDetails) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->
                new IllegalArgumentException("선택한 게시물은 없는 게시물입니다.")
        );
        // user 정보 가져오기 (id)
        Long user = userDetails.getUser().getId();
        int views = board.getViews();

        // 비공개인지 확인
        if(board.isPrivate() == true){
            // 아이디 비교
            System.out.println("user = " + user);
            System.out.println("board.getUser().getId() = " + board.getUser().getId());
            if(user.equals(board.getUser().getId())){
                // newBoard -> responseDto로 반환
                BoardCreateResponseDto responseDto = new BoardCreateResponseDto(board);
                // Creating the ApiResponse object
//                ApiResponse<BoardCreateResponseDto> response = new ApiResponse<>(200, "Board responsed successfully", responseDto);
                // Returning the response entity with the appropriate HTTP status
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            }else {
                return new ResponseEntity<>("선택한 게시물은 볼 수 없는 게시물입니다.",HttpStatus.NO_CONTENT);
            }
        }else {
            // 조회수 추가 & 저장
            views++;

            // newBoard -> responseDto로 반환
            BoardCreateResponseDto responseDto = new BoardCreateResponseDto(board, views);
            // Creating the ApiResponse object
//            ApiResponse<BoardCreateResponseDto> response = new ApiResponse<>(200, "Board responsed successfully", responseDto);
            // Returning the response entity with the appropriate HTTP status
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

    }

    /* 게시물 user_id 전체 목록 조회 (페이징) */
    // 페이징 구현 추가 필요
    public ResponseEntity<List<BoardCreateResponseDto>> findBoardByUserId(Long userId) {
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
//        ApiResponse<List<BoardCreateResponseDto>> response = new ApiResponse<>(200, "Board responsed successfully", responseDtos);
        // Returning the response entity with the appropriate HTTP status
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    /* 게시물 전체 목록 조회 (페이징) & 아이디에 해당하는 값 있으면 수정 기능 추가하기 */
    // 페이징 구현 추가 필요
    public ResponseEntity<List<BoardCreateResponseDto>> findBoardAll(UserDetailsImpl userDetails) {
        List<Board> boards = boardRepository.findAll();
        List<BoardCreateResponseDto> responseDtos = new ArrayList<>();

        User user = userDetails.getUser();
        for (Board board : boards) {
            // 비공개인지 확인
            if(board.isPrivate() == false) {
                // 아이디 비교
                if (user.equals(board.getUser().getId())) {
                    responseDtos.add(new BoardCreateResponseDto(board));
                }
            }else {
                responseDtos.add(new BoardCreateResponseDto(board));
            }
        }
        // Creating the ApiResponse object
//        ApiResponse<List<BoardCreateResponseDto>> response = new ApiResponse<>(200, "Board responsed successfully", responseDtos);
        // Returning the response entity with the appropriate HTTP status
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);

    }

    /* 게시물 수정 */
    @Transactional
    public ResponseEntity<?> updateBoard(BoardUpdateRequestDto requestDTO, UserDetailsImpl userDetails, MultipartFile image) throws IOException {

        // user 정보 가져오기 (id)
        Long userId = userDetails.getUser().getId();

        // 유저 아이디와 게시물의 id 가 같은지 확인
        Long boardUserId = requestDTO.getBoardUserId();

        if(userId == null || boardUserId == null) {
            log.info("User의 Id 값이 없습니다.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Board board = boardRepository.findById(requestDTO.getBoardId()).orElseThrow(()->
                new IllegalArgumentException("선택한 게시물은 없는 게시물입니다.")
        );;
        if(!boardUserId.equals(userId)) {
            log.info("게시물을 작성한 사용자가 아닙니다.");
            return new ResponseEntity<>("게시물을 작성한 사용자가 아닙니다", HttpStatus.BAD_REQUEST);
        }

        // 날씨 정보 저장 -> 날씨 정보 db에 이미 있는지 검증 (캐싱)
        Weather weather = weatherService.getWeatherByAddress(requestDTO.getAddressId());
        // 같으면 update 실행

        // request 로 받아 온 값 넣기 -> 뒤로
        Board updateBoard = board.update(requestDTO, weather);

        // 사진 업데이트
        if(image != null && !image.isEmpty()) {
            //1. 기존 사진을 제거해야 한다
            
            // Board Entity ImageList -> Null 설정
            updateBoard.clearBoardImages();

            //2. DB에서 지울 때 / byBoardId
            List<BoardImage> updateBoardImages = updateBoard.getBoardImages();
            
            // db 삭제 & 실제 저장 이미지 삭제
            for (BoardImage boardImage : updateBoardImages) {
                boardImageService.deleteImage(boardImage.getId());
            }

            // 3. 사진 저장 메서드 실행
            BoardImage newBoardImage = boardImageService.uploadImage(updateBoard, image);
            // 4. Board Entity에 추가
            board.getBoardImages().add(newBoardImage);

        }

        //1. Board Entity에서 태그 삭제
            updateBoard.clearBoardTags();
            // 2. db에서 태그 삭제
            boardTagRepository.deleteAll(updateBoard.getBoardTags()); // Remove existing tags
            for (ClothesRequestDTO clothesRequestDTO: requestDTO.getClothesRequestDTO()) {

                System.out.println("clothesRequestDTO.getColor() = " + clothesRequestDTO.getColor());
                System.out.println("clothesRequestDTO.getType() = " + clothesRequestDTO.getType());

                // 3. 태그 추가
                BoardTag updateBoardTag = new BoardTag(updateBoard, clothesRequestDTO.getColor(), clothesRequestDTO.getType());
                boardTagRepository.save(updateBoardTag);
                
                // 4. Board Entity에 추가
                updateBoard.getBoardTags().add(updateBoardTag);
             }

            // newBoard -> responseDto로 반환
            BoardCreateResponseDto responseDto = new BoardCreateResponseDto(updateBoard);
        // Returning the response entity with the appropriate HTTP status
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
            
    }
    /* 게시물 삭제 (게시물을 작성한 유저가 맞는지) */
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

    /* 게시물 좋아요 변경 */
    @Transactional
    public ResponseEntity<?> switchBoardLikes(Long boardId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Board board = boardRepository.findById(boardId).orElseThrow(()->
                new IllegalArgumentException("게시물이 존재하지 않습니다")
        );

        // 좋아요가 아예 없는 경우
        BoardLike newBoardLike = new BoardLike(user, board);
        if(board.getBoardLikes().isEmpty()) {
            board.getBoardLikes().add(newBoardLike);
            boardLikeRepository.save(newBoardLike);
            int boardLikes = board.getBoardLikes().size();
            return new ResponseEntity<>(boardLikes, HttpStatus.OK);
        }
        for(BoardLike boardLike : board.getBoardLikes()) {
            System.out.println("boardLike = " + boardLike);
            System.out.println("boardLike.getUser() = " + boardLike.getUser());
            System.out.println("boardLike.getBoard() = " + boardLike.getBoard());
            
        }
        // 유저가 이미 좋아요 눌렀는 지 확인 & 성능 개선 필요
        BoardLike existingLike = boardLikeRepository.findByUserAndBoard(user, board);

        if (existingLike != null) {
            // User has already liked the post; remove the like
            board.getBoardLikes().remove(existingLike);
            boardLikeRepository.delete(existingLike);
        } else {
            // User has not liked the post; add the like
            BoardLike newBoardLike2 = new BoardLike(user, board);
            board.getBoardLikes().add(newBoardLike2);
            boardLikeRepository.save(newBoardLike2);
        }
        int boardLikes = board.getBoardLikes().size();
        return new ResponseEntity<>(boardLikes, HttpStatus.OK);
    }
}
