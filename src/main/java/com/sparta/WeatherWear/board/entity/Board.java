package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.board.dto.BoardCreateRequestDto;
import com.sparta.WeatherWear.board.dto.BoardUpdateRequestDto;

import com.sparta.WeatherWear.board.time.Timestamped;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.weather.entity.Weather;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 회원 정보

    @Column(name = "address", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String address; // 주소

    @Column(name = "title", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String title; // 제목

    @Column(name = "content", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content; // 내용

    @Column(name = "is_private")
    private boolean isPrivate; //  공개여부

    @ManyToOne
    @JoinColumn(name = "weather_id", nullable = false)
    private Weather weather; // 날씨 정보

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardLike> boardLikes = new ArrayList<>(); // 게시물 좋아요

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>(); // 댓글

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardTag> boardTags = new ArrayList<>(); // 옷 태그

    @Column(name = "image", nullable = false)
    private String boardImage; // 이미지

    // 이승현 : 조회수
    @Column(name = "views", nullable = true)
    private int views; // 조회수

    // 게시물 생성
    public Board(BoardCreateRequestDto requestDto, User user, Weather weather, String imageUrl) {
        this.user = user;
        //
        this.title = requestDto.getTitle();
        this.content = requestDto.getContents();
        this.isPrivate = requestDto.isPrivate();
        //
        this.weather = weather;
        //
        this.address = requestDto.getAddress();
        this.boardImage = imageUrl;
        this.views = 0;
    }

    // 댓글 추가
    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    // 이승현 : 좋아요 수 확인
    public int getLikesSize(){
        return this.boardLikes.size();
    }

    // 이승현 : 댓글 수 확인
    public int getCommentsSize(){
        return this.comments.size();
    }

    // 업데이트 (이미지 수정)
    public Board update(BoardUpdateRequestDto requestDTO, Weather weather, String imageUrl) {
        this.weather = weather;
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContents();
        this.isPrivate = requestDTO.isPrivate();
        this.boardImage = imageUrl;
        return this;
    }

    // 보드 태그 List 비우기
    public void clearBoardTags() {
        this.boardTags.clear();
    }

    // 조회수 업데이트
    public void updateViews(int views) {
        this.views = views;
    }

    // Weather에서 Address ID 얻기
    public Long getAddressIdFromWeather() {
        return this.weather.getAddress().getId();
    }
}