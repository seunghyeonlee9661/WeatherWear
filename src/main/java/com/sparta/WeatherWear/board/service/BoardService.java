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
        // 날씨값 검색
        Weather weather = weatherService.getWeatherByAddress(requestDto.getAddress_id());
        // 게시판 정보 저장
        Board newBoard = boardRepository.save(new Board(requestDto, userDetails.getUser(), weather));
        // 이미지가 있을 경우 파일로 저장
        String image = null;

        if(!file.isEmpty()) {
            File webPFile = imageTransformService.convertToWebP(file);
            image = s3Service.uploadFile(webPFile);
        }
        newBoard.uploadImage(image);
        // 태그 정보 저장
        saveTags(newBoard,requestDto.getTags());
        return ResponseEntity.ok("Board created successfully");
    }
    
    // 게시물 수정
    @Transactional
    public ResponseEntity<String> updateBoard(BoardUpdateRequestDto requestDTO, UserDetailsImpl userDetails, MultipartFile file) throws IOException {
        Board board = boardRepository.findById(requestDTO.getId()).orElseThrow(()-> new IllegalArgumentException("선택한 게시물은 없는 게시물입니다."));

        if(!Objects.equals(board.getUser().getId(), userDetails.getUser().getId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시물 작성자만 수정을 요청할 수 있습니다.");

        String image = board.getImage();
        if(!image.isEmpty() && file.isEmpty()) s3Service.deleteFileByUrl(image);
        if(!file.isEmpty()) {
            File webPFile = imageTransformService.convertToWebP(file);
            image = s3Service.uploadFile(webPFile);
        }

        board.update(requestDTO,image);
        // 기존 태그 삭제
        boardTagRepository.deleteByBoard(board);
        saveTags(board, requestDTO.getTags());
        return ResponseEntity.ok("게시물이 업데이트 되었습니다.");

    }

    private void saveTags(Board board,List<BoardTagDTO> tags){
        for (BoardTagDTO tag : tags){
            boardTagRepository.save(new BoardTag(board,tag));
        }
    }

    @Transactional
    public ResponseEntity<?> findBoardById(Long boardId, UserDetailsImpl userDetails, HttpServletRequest request) {
        Board board = boardRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("선택한 게시물은 없는 게시물입니다."));
        User user = userDetails != null ? userDetails.getUser() : null;

        // 게시물이 비공개이고, 로그인하지 않았거나 게시물 작성자가 현재 로그인한 사용자가 아닌 경우
        if (board.isPrivate() && (user == null || !board.getUser().getId().equals(user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 게시물은 비공개 상태이므로 접근할 수 없습니다.");
        }
        // 사용자 ip를 활용해 24시간 동안 조회수 사용을 방지합니다.
        if (redisService.incrementViewCount(getClientIp(request), boardId.toString())) {
            board.incrementViews(); // Board 엔티티의 조회수 증가 메서드 호출
            boardRepository.save(board);
        }
        BoardDetailResponseDTO boardDetailResponseDTO = new BoardDetailResponseDTO(board);
        if (user != null) {
            boardDetailResponseDTO.setLike(boardLikeRepository.existsByUserAndBoard(user, board));
        }
        return ResponseEntity.ok(boardDetailResponseDTO);
    }


    public ResponseEntity<List<BoardListResponseDTO>> findBoardList(Long lastId, String search) {
        // Pageable 객체를 생성 (페이지 번호 대신 커서를 사용하여 페이지네이션)
        Pageable pageable = PageRequest.of(0, 8, Sort.by(Sort.Order.desc("id")));
        List<Board> boards = boardRepository.findBoards(lastId, search, pageable);
        return ResponseEntity.ok(boards.stream().map(BoardListResponseDTO::new).collect(Collectors.toList()));
    }

    public ResponseEntity<String> removeBoard(Long id, UserDetailsImpl userDetails) throws IOException {
        Board board = boardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("선택한 게시물은 없는 게시물입니다."));
        if(!Objects.equals(board.getUser().getId(), userDetails.getUser().getId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시물 작성자만 수정을 요청할 수 있습니다.");
        // 이미지 제거
        if(!board.getImage().isEmpty()) s3Service.deleteFileByUrl(board.getImage());
        boardRepository.delete(board);
        return ResponseEntity.ok("Board delete successfully.");
    }
    
    public ResponseEntity<String> setLike(Long id, UserDetailsImpl userDetails){
        Board board = boardRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("선택한 게시물은 없는 게시물입니다."));
        User user = userDetails.getUser();

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

    // 사용자 IP 확인
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
