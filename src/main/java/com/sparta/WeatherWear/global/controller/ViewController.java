package com.sparta.WeatherWear.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
/* 로그인 테스트 뷰 컨트롤러 : 삭제 요망 */
@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login"; // "login"은 src/main/resources/templates/login.html을 가리킴
    }
}
