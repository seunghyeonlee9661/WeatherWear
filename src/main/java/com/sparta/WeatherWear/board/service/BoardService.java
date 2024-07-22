package com.sparta.WeatherWear.board.service;

import com.sparta.WeatherWear.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BoardService {

    private BoardRepository boardRepository;


}
