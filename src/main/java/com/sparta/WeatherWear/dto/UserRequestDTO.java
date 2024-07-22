package com.sparta.WeatherWear.dto;

import com.sparta.WeatherWear.enums.UserGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.Date;

/* 사용자 생성 요청 */
@Getter
public class UserRequestDTO {

    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "비밀번호는 8-15자 길이여야 하며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다.")
    private String password;

    @NotNull(message = "지역을 선택해주세요.")
    private Integer stn;
    
    @NotBlank(message = "성별은 필수 항목입니다.")
    private UserGender gender;

    @NotNull(message = "지역을 선택해주세요.")
    private Date birthday;

}