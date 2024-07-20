package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.entity.*;
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
    private Integer stn;

    @Column(name = "gender", length = 10, nullable = false)
    private String gender;

    @Column(name = "birthday", nullable = false)
    private Date birthday;

    // Relationships
    @OneToMany(mappedBy = "user")
    private List<Board> boards;

    @OneToMany(mappedBy = "user")
    private List<BoardLike> boardLikes;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "user")
    private List<Closet> closets;

    @OneToMany(mappedBy = "user")
    private List<Wishlist> wishlists;

    @OneToMany(mappedBy = "user")
    private List<CommentLike> commentLikes;
}
