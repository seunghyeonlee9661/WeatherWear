package com.sparta.WeatherWear.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.nio.charset.StandardCharsets;
import java.util.List;

/*
작성자 : 이승현
자원 파일 관리를 위한 경로 설정 설정과 CORS 설정
 */
@Configuration
public class MVCConfiguration implements WebMvcConfigurer {

    // 오류 메시지의 경우 반환할 때 알림이 될 수 있도록 해주는 기능
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converters.add(0, stringConverter);
    }

    // CORS 설정
    // TODO : CORS가 잘 작동하도록 프론트엔드 서버 주소를 수정할 필요가 있음
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정
                .allowedOrigins("https://weatherwear-ten.vercel.app") // 허용할 출처
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true); // 쿠키와 인증 정보 허용
    }
}
