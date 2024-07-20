package com.sparta.WeatherWear.handler;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/* (미완성) 로그인 성공시 대처 핸들러 : 로그인 시 이전 페이지로 돌아가도록 구현 필요! */
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public AuthenticationSuccessHandler() {
        super();
        setDefaultTargetUrl("/");
    }
}