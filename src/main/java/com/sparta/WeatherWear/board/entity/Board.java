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
    private User user; // 사용자

    @Column(name = "title", columnDefinition = "MEDIUMTEXT", nullable = false) // 제목 필수 or "제목 없음" 가능
    private String title; // 제목

    @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false) // 내용 없음에 대한 대처 강구하세요!
    private String content; // 내용

    @Column(name = "address", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String address; // 주소

    @ManyToOne
    @JoinColumn(name = "weather_id", nullable = false)
    private Weather weather; // 날씨 정보

    @Column(name = "isPrivate", nullable = false) //Todo : 긍정형인 isPublic이 더 좋아 보이니까 수정 하시는거 추천 드립니다!
    private boolean isPrivate; // 공개 여부

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardLike> likes; // 좋아요 목록

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Comment> comments; // 댓글 목록

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<BoardTag> tags; // 태그 목록

    @Column(name = "image", nullable = false)
    private String image; // 이미지
    
    @Column(name = "views", nullable = true)
    private int views; // 조회수

    // 게시물 생성
    public Board(BoardCreateRequestDto requestDto, User user, Weather weather,String image) {
        this.user = user;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContents();
        this.isPrivate = requestDto.isPrivate();
        this.address = requestDto.getAddress();
        this.weather = weather;
        this.image = image;
        this.views = 0;
    }
    
    // 이미지 내용 업데이트
    public void uploadImage(String image){
        this.image = image;
    }

    // 좋아요 수 확인
    public int getLikesSize(){
        return this.likes.size();
    }

    // 댓글 수 확인
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

    // 좋아요 증가
    public void incrementViews(){
        views++;
    }
}
