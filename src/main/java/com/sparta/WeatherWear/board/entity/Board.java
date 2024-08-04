package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.board.dto.BoardCreateRequestDto;
import com.sparta.WeatherWear.board.dto.BoardUpdateRequestDto;

import com.sparta.WeatherWear.board.time.Timestamped;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.weather.entity.Weather;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {
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

    @Column(name = "address", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "weather_id", nullable = false)
    private Weather weather;

    @Column(name = "isPrivate", nullable = false)
    private boolean isPrivate;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardLike> likes;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardTag> tags;

    @Column(name = "image", nullable = false)
    private String image;

    // 이승현 : 조회수
    @Column(name = "views", nullable = true)
    private int views;

    // 게시물 생성
    public Board(BoardCreateRequestDto requestDto, User user, Weather weather) {
        this.user = user;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContents();
        this.isPrivate = requestDto.isPrivate();
        this.address = requestDto.getAddress();
        this.weather = weather;
        this.image = null;
        this.views = 0;
    }

    public void uploadImage(String image){
        this.image = image;
    }

    // 이승현 : 좋아요 수 확인
    public int getLikesSize(){
        return this.likes.size();
    }

    // 이승현 : 댓글 수 확인
    public int getCommentsSize(){
        return this.comments.size();
    }

    // 게시물 업데이트
    public void update(BoardUpdateRequestDto requestDTO,String image) {
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContents();
        this.isPrivate = requestDTO.isPrivate();
        this.image = image;
    }

    public boolean isPrivate(Long id){
        return Objects.equals(this.user.getId(), id) || !this.isPrivate;
    }

    public void incrementViews(){
        views++;
    }
}
