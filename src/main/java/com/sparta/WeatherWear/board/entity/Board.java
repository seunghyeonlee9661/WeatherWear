package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.board.dto.BoardCreateRequestDto;
import com.sparta.WeatherWear.board.dto.BoardUpdateRequestDto;

import com.sparta.WeatherWear.board.time.Timestamped;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;
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
    private User user;

    @Column(name = "address", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String address; // 주소

    @Column(name = "title", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @Column(name = "is_private")
    private boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "weather_id", nullable = false)
    private Weather weather;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardLike> boardLikes = new ArrayList<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardTag> boardTags = new ArrayList<>();

    // Board 엔티티에 image 리스트 필드 추가
//    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<BoardImage> boardImages = new ArrayList<>();

    @Column(name = "image", nullable = false)
    private String boardImage; // 이미지

    // 이승현 : 조회수
    @Column(name = "views", nullable = true)
    private int views;

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

    public Board update(BoardUpdateRequestDto requestDTO, Weather weather) {
        this.weather = weather;
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContents();
        this.isPrivate = requestDTO.isPrivate();
        return this;
    }

//    public void clearBoardImages() {
//        this.boardImages.clear();
//    }
    public void clearBoardTags() {
        this.boardTags.clear();
    }

    public void updateViews(int views) {
        this.views = views;
    }
}