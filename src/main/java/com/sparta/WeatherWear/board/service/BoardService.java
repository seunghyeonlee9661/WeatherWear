package com.sparta.WeatherWear.board.service;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.board.repository.BoardLikeRepository;
import com.sparta.WeatherWear.board.repository.BoardRepository;
import com.sparta.WeatherWear.board.repository.BoardTagRepository;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
import com.sparta.WeatherWear.clothes.enums.ClothesColor;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.global.service.ImageTransformService;
import com.sparta.WeatherWear.global.service.RedisService;
import com.sparta.WeatherWear.global.service.S3Service;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.weather.entity.Weather;
import com.sparta.WeatherWear.weather.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private BoardRepository boardRepository;
    private ImageTransformService imageTransformService;
    private S3Service s3Service;
    private RedisService redisService;

    /* 게시물 작성 */
    @Transactional
    public ResponseEntity<?> createBoard(BoardCreateRequestDto requestDto, UserDetailsImpl userDetails, @Valid MultipartFile image) throws IOException {

        // user 정보 가져오기 (id)
        User user = userDetails.getUser();

        // 날씨 정보 저장 -> 날씨 정보 db에 이미 있는지 검증 (캐싱)
        Weather weather = weatherService.getWeatherByAddress(requestDto.getAddressId());

        // image null 인지 확인
        if(image == null || image.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지를 첨부해주세요"); // null이면 오류
        }
        File webPFile = imageTransformService.convertToWebP(image); // imageWebp로 변환
        String imageUrl = s3Service.uploadFile(webPFile); // 이미지 저장 후 url 확인

        // request에서 받아온 값을 Board Entity로 만들기
        Board newBoard = new Board(requestDto, user, weather,imageUrl); // Weather 추가하기


        // Board Entity -> db에 저장
        boardRepository.save(newBoard);

        // 추가 - 태그 저장 메서드 실행
        for (ClothesRequestDTO clothesRequestDTO: requestDto.getTags()) {
            BoardTag newBoardTag = new BoardTag(newBoard, clothesRequestDTO.getColor(), clothesRequestDTO.getType());
            boardTagRepository.save(newBoardTag);
            // Board Entity에 추가
            newBoard.getBoardTags().add(newBoardTag);
        }

        // Prepare the response
        Map<String, Long> response = new HashMap<>();
        response.put("id", newBoard.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /* 게시물 id로 조회 */
    @Transactional
    public ResponseEntity<?> findBoardById(Long boardId, UserDetailsImpl userDetails,HttpServletRequest request) {
        Board board = boardRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("선택한 게시물은 없는 게시물입니다."));

        // user 정보 가져오기 (id)
        User user = userDetails != null ? userDetails.getUser() : null;
        log.info("현재 사용자 : {}", user);

        if (board.isPrivate() && (user == null || !board.getUser().getId().equals(user.getId()))) {
            log.info("비공개로 막힘");
            log.info("작성자 사용자 : {}", board.getUser().getId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 게시물은 비공개 상태이므로 접근할 수 없습니다.");
        }

        // Redis 기반, 사용자 IP로 게시물의 조회수를 1일 1회만 증가시킬 수 있도록 지정
        if (redisService.incrementViewCount(getClientIp(request), boardId.toString())) { // 사용자 IP를 Redis에서 검색
            board.updateViews(board.getViews()+1); // Board 엔티티의 조회수 증가 메서드 호출
        }
        BoardCreateResponseDto responseDto = new BoardCreateResponseDto(board);

        // 현재 게시물을 사용자가 좋아요 했는지 확인하고 상태를 추가합니다.
        boolean flag = boardLikeRepository.existsByUserAndBoard(user, board);

        if (flag == true) {
            responseDto.setCheckLike(true);
        }else{
            responseDto.setCheckLike(false);
        }
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 사용자 IP 확인하는 기능
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /* 게시물 전체 목록 조회 (페이징) & 아이디에 해당하는 값 있으면 수정 기능 추가하기 */
    public ResponseEntity<List<BoardCreateResponseDto>> findBoardAll(Long lastId, String color, String type,String keyword ) {
        // 페이저블 객체 : ID를 기반으로 내림차순
        Pageable pageable = PageRequest.of(0, 8, Sort.by(Sort.Order.desc("id")));

        // String 값을 Enum으로 변환, null 또는 빈 문자열 처리
        ClothesColor clothesColor = (color != null && !color.isEmpty()) ? ClothesColor.valueOf(color.toUpperCase()) : null;
        ClothesType clothesType = (type != null && !type.isEmpty()) ? ClothesType.valueOf(type.toUpperCase()) : null;

        // 결과값을 Repository에서 받아옵니다.
        List<Board> boards;

        // 커서가 없을 경우 : 최신 게시물 조회
        if (lastId == null) boards = boardRepository.findBoardsLatest(clothesColor,clothesType,pageable,keyword);
        // 커서값이 있을 경우 : lastId를 기준으로 커서 기반 페이지네이션
        else boards = boardRepository.findBoardsAfterId(lastId,clothesColor,clothesType,pageable,keyword);
        return ResponseEntity.ok(boards.stream().map(BoardCreateResponseDto::new).collect(Collectors.toList()));
    }

    @Transactional
    public ResponseEntity<?> updateBoard(BoardUpdateRequestDto requestDTO, UserDetailsImpl userDetails, MultipartFile image) throws IOException {

        Board board = boardRepository.findById(requestDTO.getId()).orElseThrow(()->
                new IllegalArgumentException("선택한 게시물은 없는 게시물입니다.")
        );;

        // user 정보 가져오기 (id)
        Long userId = userDetails.getUser().getId();

        // 유저 아이디와 게시물의 id 가 같은지 확인
//        Long boardUserId = requestDTO.getBoardUserId();
        Long boardUserId = board.getUser().getId();

        if(userId == null || boardUserId == null) {
            log.info("User의 Id 값이 없습니다.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        if(!boardUserId.equals(userId)) {
            log.info("게시물을 작성한 사용자가 아닙니다.");
            return new ResponseEntity<>("게시물을 작성한 사용자가 아닙니다", HttpStatus.BAD_REQUEST);
        }

        // ------------날씨 정보 저장 ----------------
        Weather weather = null;
        // 날씨 변경이 있으면 처리
        if(requestDTO.getAddressId() == null || board.getAddressIdFromWeather().equals(requestDTO.getAddressId())) {
            weather = board.getWeather();
        }else{
            weather = weatherService.getWeatherByAddress(requestDTO.getAddressId());
        }

        // ------------- 사진 ------------------

        Board updateBoard = null;

        // 사진 업데이트 X
        if(image == null || image.isEmpty()) {
            updateBoard = board.update(requestDTO, weather, board.getBoardImage());
        }else{
            s3Service.deleteFileByUrl(board.getBoardImage()); // 기존 이미지 제거

            File webPFile = imageTransformService.convertToWebP(image); // imageWebp로 변환

            String imageUrl = s3Service.uploadFile(webPFile); // 이미지 저장 후 url 확인
            
            // request 로 받아 온 값 넣기
            updateBoard = board.update(requestDTO, weather, imageUrl);

        }

        // ---------------- 옷 태그 --------------------
            // 1. db에서 태그 삭제
            boardTagRepository.deleteAll(updateBoard.getBoardTags()); // Remove existing tags
            //2. Board Entity에서 태그 삭제
            updateBoard.clearBoardTags();
            for (ClothesRequestDTO clothesRequestDTO: requestDTO.getTags()) {

                // 3. 태그 추가
                BoardTag updateBoardTag = new BoardTag(updateBoard, clothesRequestDTO.getColor(), clothesRequestDTO.getType());
                boardTagRepository.save(updateBoardTag);
                
                // 4. Board Entity에 추가
                updateBoard.getBoardTags().add(updateBoardTag);
             }

        // Prepare the response
        Map<String, Long> response = new HashMap<>();
        response.put("id", updateBoard.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    /* 게시물 삭제 (게시물을 작성한 유저가 맞는지) */
    public ResponseEntity<String> removeBoard(Long boardId, UserDetailsImpl userDetails) {

        Board board = boardRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("선택한 게시물을 찾을 수 없습니다."));

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

        return new ResponseEntity<>("삭제 되었습니다",HttpStatus.OK);

    }

    /* 게시물 좋아요 변경 */
    @Transactional
    public ResponseEntity<Integer> switchBoardLikes(Long boardId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Board board = boardRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("게시물이 존재하지 않습니다"));

        // 현재 게시물과, 현재 사용자가 좋아요 상태인지 확인합니다.
        Optional<BoardLike> boardLike = boardLikeRepository.findByUserAndBoard(user, board);
        if (boardLike.isPresent()) {
            // 좋아요가 있는 경우, 삭제
            boardLikeRepository.delete(boardLike.get());
        } else {
            // 좋아요가 없는 경우, 추가
            boardLikeRepository.save(new BoardLike(user, board));
        }

        return new ResponseEntity<>( boardLikeRepository.countLikesByBoardId(boardId), HttpStatus.OK);
    }

}
