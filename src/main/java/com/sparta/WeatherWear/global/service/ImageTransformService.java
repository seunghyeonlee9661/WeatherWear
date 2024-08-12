package com.sparta.WeatherWear.global.service;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
/*
작성자 : 이승현
이미지 Webp로 변환하기 위한 기능
*/
@Service
public class ImageTransformService  {

    public File convertToWebP(MultipartFile file) throws IOException {
        // Create a temporary file
        Path tempFilePath = Files.createTempFile("temp", ".tmp");
        File tempFile = tempFilePath.toFile();
        file.transferTo(tempFile);

        // Convert to WebP format
        String webPFileName = tempFilePath.getFileName().toString().replace(".tmp", ".webp");
        File webPFile = new File(tempFile.getParent(), webPFileName);
        ImmutableImage image = ImmutableImage.loader().fromFile(tempFile);
        image.output(WebpWriter.DEFAULT, webPFile);

        // Delete the temporary file
        tempFile.delete();

        return webPFile;
    }
}
