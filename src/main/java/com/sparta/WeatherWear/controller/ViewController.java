package com.sparta.WeatherWear.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
/*
작성자 : 이승현
카카오 로그인 확인하기 위한 로그인 처리 테스트 프론트 (삭제 무방)
 */
public class ViewController {
    /* 로그인 페이지 */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
