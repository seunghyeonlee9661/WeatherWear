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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            System.out.println("clothesRequestDTO.getColor() = " + clothesRequestDTO.getColor());
            System.out.println("clothesRequestDTO.getType() = " + clothesRequestDTO.getType());
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
        Board board = boardRepository.findById(boardId).orElseThrow(()->
                new IllegalArgumentException("선택한 게시물은 없는 게시물입니다.")
        );

        // user 정보 가져오기 (id)
        User user = null;
        if(userDetails != null){
            user = userDetails.getUser();
        }

        //
        int views = board.getViews();
        System.out.println("views = " + views);

        if (board.isPrivate() && (user == null || !board.getUser().getId().equals(user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이 게시물은 비공개 상태이므로 접근할 수 없습니다.");
        }

        // Redis 기반, 사용자 IP로 게시물의 조회수를 1일 1회만 증가시킬 수 있도록 지정
        if (redisService.incrementViewCount(getClientIp(request), boardId.toString())) { // 사용자 IP를 Redis에서 검색
            board.updateViews(board.getViews()+1); // Board 엔티티의 조회수 증가 메서드 호출
        }
        BoardCreateResponseDto responseDto = new BoardCreateResponseDto(board, views);

        // 현재 게시물을 사용자가 좋아요 했는지 확인하고 상태를 추가합니다.
        if (user != null) {
            responseDto.setCheckLike(boardLikeRepository.existsByUserAndBoard(user, board));
        }else{
            responseDto.setCheckLike(false);
        }



        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//        // 비공개인지 확인
//        if(board.isPrivate()){
//            if(user == null){ // 비로그인 사용자 -> 비공개
//                new ResponseEntity<>("선택한 게시물은 볼 수 없는 게시물입니다.",HttpStatus.NO_CONTENT);
//            }else{
//                if(user.getId().equals(board.getUser().getId()))
//            }
//            // 아이디 비교
////            System.out.println("user = " + user);
////            System.out.println("board.getUser().getId() = " + board.getUser().getId()); //FIXME 마찬가지로 user가 null일 경우 고려
//            if(user.equals(board.getUser().getId())){
//                // newBoard -> responseDto로 반환
//                BoardCreateResponseDto responseDto = new BoardCreateResponseDto(board);
//
//                return new ResponseEntity<>(responseDto, HttpStatus.OK);
//            }else {
//                return new ResponseEntity<>("선택한 게시물은 볼 수 없는 게시물입니다.",HttpStatus.NO_CONTENT); //FIXME HttpStatus 바꾸시면 됩니다 Forbiden 같은 걸로
//
//            }
//        }else {
//            // 조회수 추가 & 저장
//            views++;
//            board.updateViews(views);
//            // newBoard -> responseDto로 반환
//            //FIXME 중요!!!! : 여기서 BoardCreateResponseDto로 새 객체 생성하면 데이터에 반영 안됩니다. update하는 함수 board에 작성하셔야 됩니다.
//            BoardCreateResponseDto responseDto = new BoardCreateResponseDto(board, views);
//
//
//        }

    }

    // 사용자 IP 확인하는 기능
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /* 게시물 user_id 전체 목록 조회 (페이징) */
    // 페이징 구현 추가 필요
    //FIXME : 여기서도 로그인한 사용자가 자기 게시물만 볼거니까 UserDetailsImpl로 id 처리하시면 됩니다. 근데 아마 이부분은 사용자 파트라서 제가 할거 같아요.
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
    //FIXME : 커서형 페이지네이션을 하시게되면 파라미터로 커서 id(마지막 board ID)를 받으실 수 있으니 참고 바랍니다!
    public ResponseEntity<List<BoardCreateResponseDto>> findBoardAll(UserDetailsImpl userDetails, Long lastId, String search) {
    //FIXME : 여기서 아마 쿼리를 잘 짜시면 비굥개이면서 작성자 아이디가 내가 아닌 게시물 걸러내실 수 있을겁니다. GPT 추천드립니다.
        // Define the page size (e.g., 8 items per page)
        int pageSize = 8;
        // 페이저블 객체 : ID를 기반으로 내림차순
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Order.desc("id")));

        // 현재 사용자가 로그인중이면 id를 받아옵니다. 해당 변수는 비공개를 필터링 하기 위해 사용합니다.
        Long userId = null;
        if (userDetails != null){
            userId = userDetails.getUser().getId();
        }

        // 결과값을 Repository에서 받아옵니다.
        List<Board> boards;
        if (lastId == null) {
            // latest (가장 최근의 게시물부터) 조회
            boards = boardRepository.findBoardsLatest(search,userId, pageable);
        } else {
            // lastId를 기준으로 커서 기반 페이지네이션
            boards = boardRepository.findBoardsAfterId(lastId, search,userId, pageable);
        }

        return ResponseEntity.ok(boards.stream().map(BoardCreateResponseDto::new).collect(Collectors.toList()));

    }

    /* 게시물 수정 */
    //FIXME : 이것도 반환 String으로 하시면 될듯합니다.
    @Transactional
    public ResponseEntity<?> updateBoard(BoardUpdateRequestDto requestDTO, UserDetailsImpl userDetails, MultipartFile image) throws IOException {

        // user 정보 가져오기 (id)
        Long userId = userDetails.getUser().getId();

        // 유저 아이디와 게시물의 id 가 같은지 확인
        Long boardUserId = requestDTO.getBoardUserId();

        if(userId == null || boardUserId == null) {//FIXME : User가 null인 상황일 경우, 비로그인 요청일 경우 Security 필터에서 걸릴거라 이 부분도 제거하셔도 됩니다.
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

        Board updateBoard = null;

        // 사진 업데이트
        if(image != null && !image.isEmpty()) {

            // image null 인지 확인
            if(image == null || image.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지를 첨부해주세요"); // null이면 오류
            }
            s3Service.deleteFileByUrl(board.getBoardImage()); // 기존 이미지 제거

            File webPFile = imageTransformService.convertToWebP(image); // imageWebp로 변환
            String imageUrl = s3Service.uploadFile(webPFile); // 이미지 저장 후 url 확인

            // request 로 받아 온 값 넣기
            updateBoard = board.update(requestDTO, weather, imageUrl);

        }

            // 1. db에서 태그 삭제
            boardTagRepository.deleteAll(updateBoard.getBoardTags()); // Remove existing tags
            //2. Board Entity에서 태그 삭제
            updateBoard.clearBoardTags();
            for (ClothesRequestDTO clothesRequestDTO: requestDTO.getClothesRequestDTO()) {

                System.out.println("clothesRequestDTO.getColor() = " + clothesRequestDTO.getColor());
                System.out.println("clothesRequestDTO.getType() = " + clothesRequestDTO.getType());

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

        if(userId == null || boardUserId == null) { //FIXME : 여기서도 마찬가지, user의 존재 유무를 파악하실 필요는 없어 보입니다.
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
    public ResponseEntity<?> switchBoardLikes(Long boardId, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Board board = boardRepository.findById(boardId).orElseThrow(()->
                new IllegalArgumentException("게시물이 존재하지 않습니다")
        );

        // 좋아요가 아예 없는 경우
        //FIXME : 좋아요가 아예 없는 경우는 의미가 없을거 같은데요??
        BoardLike newBoardLike = new BoardLike(user, board);
        if(board.getBoardLikes().isEmpty()) {
            board.getBoardLikes().add(newBoardLike);
            boardLikeRepository.save(newBoardLike);
            int boardLikes = board.getBoardLikes().size();
            return new ResponseEntity<>(boardLikes, HttpStatus.OK);
        }

        // 유저가 이미 좋아요 눌렀는 지 확인 & 성능 개선 필요
        //FIXME : findByUserAndBoard에 결과가 없는 경우 에러가 나기 때문에 Optional<Board>로 쓰시는게 좋습니다!
        // Optional은 존재 유무를 파악할 수 있습니다! 있으면 get 가능!
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
        // FIXME : 좋아요 수 반환하도록 하신거 좋습니다!!!
        int boardLikes = board.getBoardLikes().size();

        // Prepare the response
        Map<String, Integer> response = new HashMap<>();
        response.put("boardLikes", boardLikes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> findBoardAllByCity(UserDetailsImpl userDetails, String city, Long page) {
//        address

        List<Board> allBoardByCity = boardRepository.findAllByAddress(city);

        // newBoard -> responseDto로 반환
        List<BoardCreateResponseDto> responseDtos = new ArrayList<>();

        for (Board board : allBoardByCity) {

            // isPrivate 확인

            responseDtos.add(new BoardCreateResponseDto(board));
        }

        return new ResponseEntity<>(responseDtos,HttpStatus.OK);
    }

    public ResponseEntity<?> findBoardAllByWeather(UserDetailsImpl userDetails, String weather, Long page) {
//        weather / address / id


        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> findBoardAllByColor(UserDetailsImpl userDetails, String color, Long page) {
//        color
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> findBoardAllByType(UserDetailsImpl userDetails, String type, Long page) {
//            type
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
