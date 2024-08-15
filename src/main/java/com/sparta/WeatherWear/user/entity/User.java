package com.sparta.WeatherWear.user.entity;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.board.entity.Comment;
import com.sparta.WeatherWear.clothes.entity.Clothes;
import com.sparta.WeatherWear.user.dto.UserCreateRequestDTO;
import com.sparta.WeatherWear.user.enums.UserGender;
import com.sparta.WeatherWear.wishlist.entity.Wishlist;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
/*
작성자 : 이승현
 사용자 Entity
 */
@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이메일
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    // 닉네임
    @Column(name = "nickname", length = 255, nullable = false, unique = true)
    private String nickname;

    // 패스워드
    @Column(name = "password", nullable = false)
    private String password;

    // 사용자 이미지 url
    @Column(name = "image", nullable = true)
    private String image;

    // 성별
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    // 생일
    @Column(name = "birthday", nullable = true)
    private Date birthday;

    // 카카오 아이디
    @Column(name = "kakao_id", nullable = true)
    private Long kakaoId;

    // 사용자의 게시물
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Board> boards;

    // 사용자가 좋아요한 게시물
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardLike> boardLikes;

    // 사용자의 댓글
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    // 사용자의 옷 목록
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Clothes> clothes;

    // 사용자의 위시리스트 목록
    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Wishlist> wishlists;


    /* 기본 유저 회원가입 */
    public User(UserCreateRequestDTO userCreateRequestDTO, String password) {
        this.email = userCreateRequestDTO.getEmail();
        this.password = password;
        this.nickname = userCreateRequestDTO.getNickname();
        this.gender = UserGender.valueOf(userCreateRequestDTO.getGender().toUpperCase());
        this.birthday = userCreateRequestDTO.getBirthday();
    }

    /* 카카오 유저 생성*/
    public User(String password, String email, String nickname, Long kakaoId, String image){
        this.kakaoId = kakaoId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
    }

    /* 카카오 아이디 업데이트 */
    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    /* 사용자 정보 변경*/
    public void updateInfo(String nickname,String imageUrl){
        this.nickname = nickname;
        this.image = imageUrl;
    }

    /* 사용자 정보 변경*/
    public void updatePassword(String password){
        this.password = password;
    }
}
