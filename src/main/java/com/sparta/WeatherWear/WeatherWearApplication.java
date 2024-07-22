package com.sparta.WeatherWear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WeatherWearApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherWearApplication.class, args);
	}

}
