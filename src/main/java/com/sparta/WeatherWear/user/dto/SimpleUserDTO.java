package com.sparta.WeatherWear.user.dto;

import com.sparta.WeatherWear.user.entity.User;
import lombok.Getter;

@Getter
public class SimpleUserDTO {
    private Long id;
    private String email;
    private String nickname;
    private String image;

    public SimpleUserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.image = user.getImage();
    }
}

