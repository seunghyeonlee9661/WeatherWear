package com.sparta.WeatherWear.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.Date;

/*
작성자 : 이승현
사용자 회원가입 요청 DTO
 */
@Getter
public class UserUpdateRequestDTO {

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @NotBlank(message = "성별은 필수 항목입니다.")
    private String gender;

    @NotNull(message = "생년월일을 선택해주세요.")
    private Date birthday;

}
