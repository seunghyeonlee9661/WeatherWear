package com.sparta.WeatherWear.board.service;

import com.sparta.WeatherWear.board.dto.ApiResponse;
import com.sparta.WeatherWear.board.dto.BoardCreateRequestDto;
import com.sparta.WeatherWear.board.dto.BoardCreateResponseDto;
import com.sparta.WeatherWear.board.dto.BoardfindRequestDto;
import com.sparta.WeatherWear.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private BoardRepository boardRepository;


    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> createBoard(BoardCreateRequestDto requestDTO) {
    }

    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> findBoardById(Long userId, Long boardId) {
    }

    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardByUserId(Long userId) {
    }

    public ResponseEntity<ApiResponse<List<BoardCreateResponseDto>>> findBoardAll(Long userId, BoardfindRequestDto requestDTO) {
    }

    public ResponseEntity<ApiResponse<BoardCreateResponseDto>> updateBoard(Long userId) {
    }

    public ResponseEntity<String> removeBoard(Long userId) {
    }


}
