package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardLike;
import com.sparta.WeatherWear.dto.UserRequestDTO;
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

    @Column(name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "stn", nullable = false)
    private int stn;

    @Column(name = "gender", length = 10, nullable = false)
    private UserGender gender;

    @Column(name = "birthday", nullable = false)
    private Date birthday;

    // Relationships
//    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
//    private List<Board> boards;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardLike> boardLikes;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Clothes> clothes;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Wishlist> wishlists;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikes;

    public User(UserRequestDTO userRequestDTO, String password) {
        this.email = userRequestDTO.getEmail();
        this.password = password;
        this.stn = userRequestDTO.getStn();
        this.gender = userRequestDTO.getGender();
        this.birthday = userRequestDTO.getBirthday();
    }

    public void update(UserRequestDTO userRequestDTO){
        this.email = userRequestDTO.getEmail();
        this.stn = userRequestDTO.getStn();
        this.gender = userRequestDTO.getGender();
        this.birthday = userRequestDTO.getBirthday();
    }
}
