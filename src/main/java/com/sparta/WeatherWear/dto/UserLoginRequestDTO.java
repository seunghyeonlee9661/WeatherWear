package com.sparta.WeatherWear.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

/* 사용자 로그인 요청 */
@Getter
public class UserLoginRequestDTO {

    @NotBlank(message = "아이디를 입력해주세요")
    @Email(message = "유효한 이메일 주소여야 합니다.")
    private String username;

    @NotNull(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "The password must be 8-15 characters long and include at least one letter, one number, and one special character.")
    private String password;
}
