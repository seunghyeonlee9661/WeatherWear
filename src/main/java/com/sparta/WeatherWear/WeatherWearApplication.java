package com.sparta.WeatherWear;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = "com.sparta.WeatherWear") // 필요한 패키지 경로 지정
public class WeatherWearApplication {
	public static void main(String[] args) {
		SpringApplication.run(WeatherWearApplication.class, args);
	}
}
