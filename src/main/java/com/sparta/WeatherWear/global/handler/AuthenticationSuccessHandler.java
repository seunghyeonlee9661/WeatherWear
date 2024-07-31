package com.sparta.WeatherWear.global.handler;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
/*
작성자 : 이승현
로그인 성공시 대처 핸들러 : 로그인 시 이전 페이지로 돌아가도록 구현 필요!
*/
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public AuthenticationSuccessHandler() {
        super();
        setDefaultTargetUrl("/");
    }
}