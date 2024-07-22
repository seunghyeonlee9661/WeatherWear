package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import java.util.Date;
import java.util.List;

@Getter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "regist_date", nullable = false)
    private Date registDate;

    @Column(name = "update_date", nullable = false)
    private Date updateDate;

    @Column(name = "contents", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String contents;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLikes;
}
