package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.board.dto.BoardCreateRequestDto;
import com.sparta.WeatherWear.board.dto.BoardUpdateRequestDto;
import com.sparta.WeatherWear.time.Timestamped;
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

    @Column(name = "title", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @Column(name = "isPrivate", nullable = false)
    private boolean isPrivate;

//    @Column(name = "stn", nullable = false)
//    private int stn;

    @ManyToOne
    @JoinColumn(name = "weather_id", nullable = false)
    private Weather weather;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardLike> boardLikes;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardTag> boardTags;

    // Board 엔티티에 image 리스트 필드 추가
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardImage> boardImages = new ArrayList<>();

    // 이승현 : 조회수
    @Column(name = "views", nullable = true)
    private int views;

    public Board(BoardCreateRequestDto requestDto, User user, Weather weather) {
        this.user = user;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContents();
        this.isPrivate = requestDto.isPrivate();
        this.weather = weather;
    }

    public Board update(BoardUpdateRequestDto requestDTO){
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContents();
        this.isPrivate = requestDTO.isPrivate();
//        this.stn = requestDTO.getStn();
        this.boardLikes = requestDTO.getBoardLikes();
        this.comments = requestDTO.getComments();
        this.boardTags = requestDTO.getBoardTags();
        return this;
    }

    public int getLikesSize(){
        return this.boardLikes.size();
    }

    public int getCommentsSize(){
        return this.comments.size();
    }
}
