package com.sparta.WeatherWear.global.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
작성자 : 이승현
* S3에 파일을 업로드 하거나 삭제하는 역할을 수행합니다.
*/
@Service
public class S3Service {
    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

     /* 파일 업로드 */
    public String uploadFile(File file) throws IOException {
        String key = generateUniqueFileName();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (InputStream inputStream = Files.newInputStream(file.toPath())) {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.length()));
        }
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

    /* S3에 없는 유니크 이름을 생성하는 기능 */
    private String generateUniqueFileName() {
        String key;
        do {
            key = UUID.randomUUID().toString() + ".webp";
        } while (fileExistsInS3(key));
        return key;
    }

    /* 해당 이름이 현재 S3이 있는지 확인 */
    private boolean fileExistsInS3(String key) {
        try {
            s3Client.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    /* 파일 삭제 기능 */
    public void deleteFileByUrl(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

    /* url로 부터 파일 이름을 추출하는 기능 */
    private String extractKeyFromUrl(String fileUrl) {
        String pattern = String.format("https://%s.s3.%s.amazonaws.com/(.*)", bucketName, region);
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(fileUrl);
        if (m.find()) {
            return m.group(1);
        } else {
            throw new IllegalArgumentException("Invalid S3 URL: " + fileUrl);
        }
    }
}
