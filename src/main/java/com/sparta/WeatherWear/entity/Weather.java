package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "board")
    private List<Board> boards = new ArrayList<Board>();

    @Column(name = "stn", nullable = false)
    private int stn;

    @Column(name = "ta", nullable = false)
    private int ta;

    @Column(name = "sky", nullable = false)
    private String sky;

    @Column(name = "prep", nullable = false)
    private int prep;
}
