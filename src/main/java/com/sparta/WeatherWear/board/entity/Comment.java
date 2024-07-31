package com.sparta.WeatherWear.board.entity;

import com.sparta.WeatherWear.time.Timestamped;
import com.sparta.WeatherWear.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

//    @Column(name = "regist_date", nullable = false)
//    private Date registDate;
//
//    @Column(name = "update_date", nullable = false)
//    private Date updateDate;

    @Column(name = "contents", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String contents;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> commentLikes;

    public Comment(User user, Board board, String contents) {
        this.user = user;
        this.board = board;
        this.contents = contents;
    }
}
