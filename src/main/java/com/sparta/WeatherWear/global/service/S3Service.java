package com.sparta.WeatherWear.global.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    public String uploadFile(MultipartFile file) throws IOException {
        String key = generateUniqueFileName();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
        }

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

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

    private String generateUniqueFileName() {
        String key;
        do {
            key = UUID.randomUUID().toString() + ".webp";
        } while (fileExistsInS3(key));
        return key;
    }

    private boolean fileExistsInS3(String key) {
        try {
            s3Client.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    public void deleteFileByUrl(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

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
