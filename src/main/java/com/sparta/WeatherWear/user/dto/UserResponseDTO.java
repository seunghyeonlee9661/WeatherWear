package com.sparta.WeatherWear.user.dto;

import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.user.enums.UserGender;
import jakarta.persistence.*;

import java.util.Date;

public class UserResponseDTO {
    private Long id;
    private String email;
    private String nickname;
    private String image;
    private UserGender gender;
    private Date birthday;

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.image = user.getImage();
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
    }
}
