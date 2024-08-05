package com.sparta.WeatherWear.global.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final String senderEmail = "WeatherWear@gmail.com"; // 발송자 이메일 주소
    private final String senderName = "WW_Admin"; // 발송자 이름

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
            message.setText("To reset your password, please use the following link: " + "http://your-website.com/reset-password?token=" + token);
            emailSender.send(message); // 이메일 전송
            return true; // 전송 성공
        } catch (Exception e) {
            // 이메일 전송 실패 시 예외 처리
            e.printStackTrace(); // 또는 로깅 처리
            return false; // 전송 실패
        }
    }
}
