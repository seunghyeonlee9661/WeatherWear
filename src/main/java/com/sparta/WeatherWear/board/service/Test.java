//package com.sparta.WeatherWear.board.service;
//
//import com.sparta.WeatherWear.board.dto.BoardCreateResponseDto;
//import com.sparta.WeatherWear.board.dto.BoardUpdateRequestDto;
//import com.sparta.WeatherWear.board.entity.Board;
//import com.sparta.WeatherWear.board.entity.BoardImage;
//import com.sparta.WeatherWear.board.entity.BoardTag;
//import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
//import com.sparta.WeatherWear.global.security.UserDetailsImpl;
//import com.sparta.WeatherWear.weather.entity.Weather;
//import jakarta.transaction.Transactional;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@Service
//@AllArgsConstructor
//@Slf4j
//public class Test {
//
//    /* 게시물 수정 */
//    @Transactional
//    public ResponseEntity<?> updateBoard(BoardUpdateRequestDto requestDTO, UserDetailsImpl userDetails, MultipartFile image) throws IOException {
//
//        // user 정보 가져오기 (id)
//        Long userId = userDetails.getUser().getId();
//
//        // 유저 아이디와 게시물의 id 가 같은지 확인
//        Long boardUserId = requestDTO.getUserId();
//
//        if(userId == null || boardUserId == null) {
//            log.info("User의 Id 값이 없습니다.");
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        // 날씨 정보 저장 -> 날씨 정보 db에 이미 있는지 검증 (캐싱)
//        Weather weather = weatherService.getWeatherByAddress(requestDTO.getAddressId());
//
//        // 같으면 update 실행
//        if(boardUserId.equals(userId)) {
//            // 수정할 board을 가져오기
//            Board board = boardRepository.findById(requestDTO.getBoardId()).orElseThrow(()->
//                    new IllegalArgumentException("선택한 게시물은 없는 게시물입니다.")
//            );;
//            // request 로 받아 온 값 넣기
//            Board updateBoard = board.update(requestDTO, weather);
//
//            // 사진 업데이트
//            if(image != null) {
//                // 기존 사진을 제거해야 한다
//                List<BoardImage> boardImages = updateBoard.getBoardImages();
//                boardImageRepository.deleteAll(boardImages);
//
//                // 추가 - 사진 저장 메서드 실행
//                BoardImage boardImagePath = boardImageService.uploadImage(updateBoard, image);
//                // Board Entity에 추가
//                updateBoard.getBoardImages().add(boardImagePath);
//
//                for (BoardImage boardImage : boardImages) {
//                    System.out.println("boardImage_path = " + boardImage.getImagePath());
//                }
//            }
//            // 기존 태그 삭제
//
//            // 추가 - 태그 저장 메서드 실행
//            for (ClothesRequestDTO clothesRequestDTO: requestDTO.getClothesRequestDTO()) {
//                System.out.println("clothesRequestDTO.getColor() = " + clothesRequestDTO.getColor());
//                System.out.println("clothesRequestDTO.getType() = " + clothesRequestDTO.getType());
//                BoardTag updateBoardTag = new BoardTag(updateBoard, clothesRequestDTO.getColor(), clothesRequestDTO.getType());
//                boardTagRepository.save(updateBoardTag);
//                // Board Entity에 추가
//                updateBoard.getBoardTags().add(updateBoardTag);
//            }
//
//            // newBoard -> responseDto로 반환
//            BoardCreateResponseDto responseDto = new BoardCreateResponseDto(updateBoard);
//            // Creating the ApiResponse object
////            ApiResponse<BoardCreateResponseDto> response = new ApiResponse<>(200, "Board updated successfully", responseDto);
//            // Returning the response entity with the appropriate HTTP status
//            return new ResponseEntity<>(responseDto, HttpStatus.OK);
//
//        }else{
//            log.info("게시물을 작성한 사용자가 아닙니다");
//            return new ResponseEntity<>("게시물을 작성한 사용자가 아닙니다", HttpStatus.BAD_REQUEST);
//        }
//
//
//    }
//}
