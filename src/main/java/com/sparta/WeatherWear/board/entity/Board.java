package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.entity.Comment;
import com.sparta.WeatherWear.entity.User;
import com.sparta.WeatherWear.entity.Weather;
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

    @Column(name = "title", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @Column(name = "isPrivate", nullable = false)
    private boolean isPrivate;

    @Column(name = "stn", nullable = false)
    private int stn;

    @Column(name = "regist_date", nullable = false)
    private Date registDate;

    @Column(name = "update_date", nullable = false)
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "weather_id", nullable = false)
    private Weather weather;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardLike> boardLikes;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardTag> boardTags;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardImage> boardImages;
}
