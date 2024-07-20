package com.sparta.WeatherWear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.Date;
import java.util.List;

@Getter
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "contents", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String contents;

    @Column(name = "share", nullable = false)
    private Byte share;

    @Column(name = "stn", nullable = false)
    private Integer stn;

    @Column(name = "regist_date", nullable = false)
    private Date registDate;

    @Column(name = "update_date", nullable = false)
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "weather_id", nullable = false)
    private Weather weather;

    @OneToMany(mappedBy = "board")
    private List<BoardLike> boardLikes;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

    @OneToMany(mappedBy = "board")
    private List<BoardTag> boardTags;

    @OneToMany(mappedBy = "board")
    private List<BoardImage> boardImages;
}
