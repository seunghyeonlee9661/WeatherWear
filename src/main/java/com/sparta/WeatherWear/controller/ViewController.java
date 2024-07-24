package com.sparta.WeatherWear.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("")
public class ViewController {
    /* 로그인 페이지 */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
