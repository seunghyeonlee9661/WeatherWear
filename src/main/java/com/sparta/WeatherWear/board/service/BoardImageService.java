package com.sparta.WeatherWear.board.service;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardImage;
import com.sparta.WeatherWear.board.repository.BoardImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/*
  작성자 : 하준영
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BoardImageService {

    @Autowired
    private BoardImageRepository boardImageRepository;

    public BoardImage uploadImage(Board newBoard, MultipartFile image) throws IOException {
            // 이미지 파일 저장을 위한 경로 설정
            String uploadsDir = "src/main/resources/static/uploads/Board/images/";

            // 각 이미지 파일에 대해 업로드 및 DB 저장 수행

            // 이미지 파일 경로를 저장
            String dbFilePath = saveImage(image, uploadsDir);

            // ProductThumbnail 엔티티 생성 및 저장
            BoardImage boardImage = new BoardImage(newBoard, dbFilePath);
            boardImageRepository.save(boardImage);
            return boardImage;
    }
    // 이미지 파일을 저장하는 메서드
    private String saveImage(MultipartFile image, String uploadsDir) throws IOException {
        // 파일 이름 생성
        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + image.getOriginalFilename();
        // 실제 파일이 저장될 경로
        String filePath = uploadsDir + fileName;
        // DB에 저장할 경로 문자열
        String dbFilePath = "/uploads/Board/images/" + fileName;

        Path path = Paths.get(filePath); // Path 객체 생성
        Files.createDirectories(path.getParent()); // 디렉토리 생성
        Files.write(path, image.getBytes()); // 디렉토리에 파일 저장

        return dbFilePath;
    }

    // 이미지 삭제 메서드
    public void deleteImage(Long boardImageId) throws IOException {
        BoardImage boardImage = boardImageRepository.findById(boardImageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board image ID: " + boardImageId));

        // Delete the image file from the filesystem
        deleteImageFile(boardImage.getImagePath());

        // Remove the BoardImage entity from the database
        boardImageRepository.delete(boardImage);
    }

    // 이미지 파일 실제 저장 공간에서 삭제
    private void deleteImageFile(String dbFilePath) throws IOException {
        // Convert the DB path to the filesystem path
        String filePath = "src/main/resources/static" + dbFilePath;

        Path path = Paths.get(filePath);
        Files.deleteIfExists(path); // Delete the file if it exists
    }
}
