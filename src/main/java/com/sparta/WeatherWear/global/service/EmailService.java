package com.sparta.WeatherWear.global.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
/*
작성자 : 이승현
사용자 비밀번호 찾기를 위해 사용하는 SMTP 서비스
*/
@Service
public class EmailService {
    private final String senderEmail = "WeatherWear@gmail.com"; // 발송자 이메일 주소
    private final String senderName = "WeatherWear"; // 발송자 이름
    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public boolean sendPasswordResetEmail(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom(String.format("%s <%s>", senderName, senderEmail)); // 발송자 이름과 이메일 주소 설정
            message.setSubject("Password Reset Request");
            //TODO : 사용자에게 비밀번호 재설정을 위해 전송할 웹 페이지 수정 필요
            message.setText("To reset your password, please use the following link: " + "https://weatherwearclothing.com/login/find?token=" + token);
            emailSender.send(message); // 이메일 전송
            return true; // 전송 성공
        } catch (Exception e) { // 이메일 전송 실패 시 예외 처리
            e.printStackTrace(); // 또는 로깅 처리
            return false; // 전송 실패
        }
    }
}
