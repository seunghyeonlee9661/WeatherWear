package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.dto.user.UserRequestDTO;
import com.sparta.WeatherWear.enums.UserGender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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

    @Column(name = "nickname", length = 255, nullable = false, unique = true)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "gender", length = 10, nullable = true)
    private UserGender gender;

    @Column(name = "image", length = 10, nullable = true)
    private String image;

    @Column(name = "stn", nullable = true)
    private int stn;

    @Column(name = "birthday", nullable = true)
    private Date birthday;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Board> boards;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardLike> boardLikes;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikes;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Clothes> clothes;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Wishlist> wishlists;

    private Long kakaoId;

    /* 기본 유저 회원가입 */
    public User(UserRequestDTO userRequestDTO, String password) {
        this.email = userRequestDTO.getEmail();
        this.password = password;
        this.nickname = userRequestDTO.getNickname();
        this.stn = userRequestDTO.getStn();
        this.gender = UserGender.valueOf(userRequestDTO.getGender().toUpperCase());
        this.birthday = userRequestDTO.getBirthday();
    }

    /* 카카오 유저 생성*/
    public User(String password, String email, String nickname, Long kakaoId){
        this.kakaoId = kakaoId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    /* 카카오 아이디 업데이트 */
    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    /* 사용자 정보 변경*/
    public void update(UserRequestDTO userRequestDTO){
        this.email = userRequestDTO.getEmail();
        this.nickname = userRequestDTO.getNickname();
        this.stn = userRequestDTO.getStn();
        this.gender = UserGender.valueOf(userRequestDTO.getGender().toUpperCase());
        this.birthday = userRequestDTO.getBirthday();
    }
}
