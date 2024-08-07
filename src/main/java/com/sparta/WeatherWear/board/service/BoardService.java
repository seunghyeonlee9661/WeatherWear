package com.sparta.WeatherWear.board.service;

import com.sparta.WeatherWear.board.dto.*;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.board.repository.BoardLikeRepository;
import com.sparta.WeatherWear.board.repository.BoardRepository;
import com.sparta.WeatherWear.board.repository.BoardTagRepository;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.global.service.ImageTransformService;
import com.sparta.WeatherWear.global.service.RedisService;
import com.sparta.WeatherWear.global.service.S3Service;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.weather.entity.Weather;
import com.sparta.WeatherWear.weather.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BoardService {

    private WeatherService weatherService;
    private S3Service s3Service;
    private BoardRepository boardRepository;
    private BoardTagRepository boardTagRepository;
    private BoardLikeRepository boardLikeRepository;
    private RedisService redisService;
    private final ImageTransformService imageTransformService;

    // 게시물 생성 
    @Transactional
    public ResponseEntity<String> createBoard(BoardCreateRequestDto requestDto, UserDetailsImpl userDetails,MultipartFile file) throws IOException {
        Weather weather = weatherService.getWeatherByAddress(requestDto.getAddress_id()); // 날씨값 검색

        // TODO : 게시물 이미지는 필수로 하자고 프론트에서 말하셔서 아마 nullable false로 작업하시면 될겁니다!
        if(file.isEmpty()) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시물 작성자만 수정을 요청할 수 있습니다.");
        }
        File webPFile = imageTransformService.convertToWebP(file); // Webp로 변환
        String image = s3Service.uploadFile(webPFile); // 이미지를 S3에 저장하고 url 반환
        
        // 게시물 데이터 생성
        Board newBoard = boardRepository.save(new Board(requestDto, userDetails.getUser(), weather,image)); // 게시판 정보 저장
        // 태그 내용 저장
        saveTags(newBoard,requestDto.getTags());
        return ResponseEntity.ok("Board created successfully");
    }
    
    // 게시물 수정
    @Transactional
    public ResponseEntity<String> updateBoard(BoardUpdateRequestDto requestDTO, UserDetailsImpl userDetails, MultipartFile file) throws IOException {
        // 게시물 데이터 확인
        Board board = boardRepository.findById(requestDTO.getId()).orElseThrow(()-> new IllegalArgumentException("선택한 게시물은 없는 게시물입니다."));
        // 사용자 권한 확인
        if(!Objects.equals(board.getUser().getId(), userDetails.getUser().getId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시물 작성자만 수정을 요청할 수 있습니다.");

        // 기존 이미지 image 저장
        String image = board.getImage();
        if(!file.isEmpty()) { // 새로 이미지를 보냈음 -> 이미지 변경
            s3Service.deleteFileByUrl(image); // 기존 이미지 제거
            File webPFile = imageTransformService.convertToWebP(file); // 새 이미지를 Webp로 변환
            image = s3Service.uploadFile(webPFile); // 이미지를 저장하고 url을 받음
        }
        // 변경사항 저장
        board.update(requestDTO,image);
        
        // 기존 태그 삭제
        boardTagRepository.deleteByBoard(board);
        // 새롭게 태그로 저장 : 이 부분은 "수정의 경우 기존 태그를 그대로 적어주셔야 한다고 프론트에 말씀하시면 됩니다."
        saveTags(board, requestDTO.getTags());

        return ResponseEntity.ok("게시물이 업데이트 되었습니다.");

    }

    // 태그 데이터를 저장합니다.
    private void saveTags(Board board,List<BoardTagDTO> tags){
        for (BoardTagDTO tag : tags){
            boardTagRepository.save(new BoardTag(board,tag));
        }
    }

    // 게시물 상세 정보를 반환합니다.
    @Transactional
    public ResponseEntity<?> findBoardById(Long boardId, UserDetailsImpl userDetails, HttpServletRequest request) {
        Board board = boardRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("선택한 게시물은 없는 게시물입니다."));
        User user = userDetails != null ? userDetails.getUser() : null;

        // 게시물이 비공개이고, 로그인하지 않았거나 게시물 작성자가 아닌 경우 -> 접근 거부
        if (board.isPrivate() && (user == null || !board.getUser().getId().equals(user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 게시물은 비공개 상태이므로 접근할 수 없습니다.");
        }
        // 사용자 ip를 활용해 24시간 동안 조회수 증가를 방지합니다.
        if (redisService.incrementViewCount(getClientIp(request), boardId.toString())) { // 사용자 IP를 Redis에서 검색
            board.incrementViews(); // Board 엔티티의 조회수 증가 메서드 호출
        }
        // 게시물 반환을 위한 DTO 선언
        BoardDetailResponseDTO boardDetailResponseDTO = new BoardDetailResponseDTO(board);

        // 현재 게시물을 사용자가 좋아요 했는지 확인하고 상태를 추가합니다.
        if (user != null) {
            boardDetailResponseDTO.setLike(boardLikeRepository.existsByUserAndBoard(user, board));
        }else{
            boardDetailResponseDTO.setLike(false);
        }
        return ResponseEntity.ok(boardDetailResponseDTO);
    }

    /* 게시물의 목록을 반환합니다. */
    // TODO : 현재 게시물이 비공개인 경우는 적용하지 않았습니다.
    // 비공개 게시물을 제외하고 싶다면 추가적인 작업을 해주시면 됩니다.
    public ResponseEntity<List<BoardListResponseDTO>> findBoardList(Long lastId, String search) {
        // Pageable 객체를 생성 (페이지 번호 대신 커서를 사용하여 페이지네이션)
        Pageable pageable = PageRequest.of(0, 8, Sort.by(Sort.Order.desc("id")));

        List<Board> boards;
        if (lastId == null) {
            // latest (가장 최근의 게시물부터) 조회
            boards = boardRepository.findBoardsLatest(search, pageable);
        } else {
            // lastId를 기준으로 커서 기반 페이지네이션
            boards = boardRepository.findBoardsAfterId(lastId, search, pageable);
        }
        return ResponseEntity.ok(boards.stream().map(BoardListResponseDTO::new).collect(Collectors.toList()));
    }

    // 게시믈 제거
    @Transactional
    public ResponseEntity<String> removeBoard(Long id, UserDetailsImpl userDetails) {
        // 게시물 데이터 확인
        Board board = boardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("선택한 게시물은 없는 게시물입니다."));
        // 게시물 작성자 권한 확인
        if(!Objects.equals(board.getUser().getId(), userDetails.getUser().getId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시물 작성자만 수정을 요청할 수 있습니다.");
        // 게시물 이미지 삭제
        s3Service.deleteFileByUrl(board.getImage());
        // 게시물 데이터 삭제 : 좋아요나 태그, 댓글의 경우 함께 삭제되는지 확인!
        boardRepository.delete(board);
        return ResponseEntity.ok("Board delete successfully.");
    }
    
    // 좋아요 설정
    public ResponseEntity<String> setLike(Long id, UserDetailsImpl userDetails){
        Board board = boardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("선택한 게시물은 없는 게시물입니다."));
        User user = userDetails.getUser();

        // 현재 게시물과, 현재 사용자가 좋아요 상태인지 확인합니다.
        Optional<BoardLike> boardLike = boardLikeRepository.findByUserAndBoard(user, board);
        if (boardLike.isPresent()) {
            // 좋아요가 있는 경우, 삭제
            boardLikeRepository.delete(boardLike.get());
            return ResponseEntity.ok("좋아요가 취소되었습니다.");
        } else {
            // 좋아요가 없는 경우, 추가
            boardLikeRepository.save(new BoardLike(user, board));
            return ResponseEntity.ok("좋아요가 설정되었습니다.");
        }
    }

    // 사용자 IP 확인하는 기능
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
