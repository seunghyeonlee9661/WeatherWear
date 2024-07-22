package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.board.dto.BoardUpdateRequestDto;
import com.sparta.WeatherWear.dto.UserRequestDTO;
import com.sparta.WeatherWear.entity.Comment;
import com.sparta.WeatherWear.entity.User;
import com.sparta.WeatherWear.entity.Weather;
import com.sparta.WeatherWear.time.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import java.util.Date;
import java.util.List;

@Getter
@Entity
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "title", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @Column(name = "isPrivate", nullable = false)
    private boolean isPrivate;

    @Column(name = "stn", nullable = false)
    private int stn;

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

    public Board update(BoardUpdateRequestDto requestDTO){
        this.title = requestDTO.getTitle();
        this.content = requestDTO.getContents();
        this.isPrivate = requestDTO.isPrivate();
        this.stn = requestDTO.getStn();
        this.weather = requestDTO.getWeather();
        this.boardLikes = requestDTO.getBoardLikes();
        this.comments = requestDTO.getComments();
        this.boardTags = requestDTO.getBoardTags();
        this.boardImages = requestDTO.getBoardImages();
        return this;
    }
}
