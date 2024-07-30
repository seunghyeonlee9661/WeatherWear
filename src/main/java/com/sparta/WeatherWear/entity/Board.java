package com.sparta.WeatherWear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
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

    @CreatedDate
    @Column(name = "regist_date", nullable = false)
    private Date registDate;

    @LastModifiedDate
    @Column(name = "update_date", nullable = true)
    private Date updateDate;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "views", nullable = true)
    private int views;

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

    public int getLikesSize(){
        return this.boardLikes.size();
    }

    public int getCommentsSize(){
        return this.comments.size();
    }
}
