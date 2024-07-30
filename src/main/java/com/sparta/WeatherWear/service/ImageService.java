package com.sparta.WeatherWear.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    @Value("${image.upload.dir}") // 이미지 저장 디렉토리를 설정 파일에서 주입 받음
    private String uploadDir;

    public String uploadImagefile(String filename, MultipartFile file) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename); // 저장할 경로
        System.out.println(filePath);
        // 디렉토리가 존재하지 않으면 생성
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        // 파일 저장
        file.transferTo(filePath.toFile());
        // 이미지 URL 생성
        return "/images/" + filename;
    }
}
