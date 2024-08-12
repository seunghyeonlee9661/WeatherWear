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
}
