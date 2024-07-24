package com.sparta.WeatherWear.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/* 이미지 전송을 위해 FTP를 사용해 업로드와 다운로드를 진행합니다. */
@Service
public class FTPService {

    private static final Logger logger = LoggerFactory.getLogger(FTPService.class);

    @Value("${ftp.server}")
    private String ftpServer;

    @Value("${ftp.port}")
    private int ftpPort;

    @Value("${ftp.user}")
    private String ftpUser;

    @Value("${ftp.password}")
    private String ftpPassword;

    @Value("${ftp.uploadDir}")
    private String ftpUploadDir;

    public boolean uploadImageToFtp(String filename, MultipartFile file) throws IOException {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpServer, ftpPort);
            ftpClient.login(ftpUser, ftpPassword);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            logger.info("FTP Upload Directory: {}", ftpUploadDir);
            // FTP 서버의 디렉토리로 이동
            boolean changeDir = ftpClient.changeWorkingDirectory(ftpUploadDir);
            if (!changeDir) {
                throw new IOException("Failed to change directory on FTP server: " + ftpUploadDir);
            }
            // 파일 업로드
            System.out.println("File uploaded start to FTP server: " + ftpUploadDir + filename);
            InputStream inputStream = file.getInputStream();
            if (inputStream == null) {
                throw new IOException("Failed to get input stream from file");
            }
            boolean uploaded = ftpClient.storeFile(filename, inputStream);
            inputStream.close();
            if (uploaded) {
                System.out.println("File uploaded successfully to FTP server: " + ftpUploadDir + filename);
            } else {
                // 오류 메시지 출력
                String reply = ftpClient.getReplyString();
                System.out.println("Failed to upload file to FTP server. Reply: " + reply);
            }
            return uploaded;
        } finally {
            // FTP 연결 종료
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    // 오류가 발생해도 무시
                    ex.printStackTrace();
                }
            }
        }
    }
}

