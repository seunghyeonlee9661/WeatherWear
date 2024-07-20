package com.sparta.WeatherWear.entity;

import com.sparta.WeatherWear.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import java.util.Date;
import java.util.List;

@Getter
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stn", nullable = false)
    private Integer stn;

    @Column(name = "ta", nullable = false)
    private Integer ta;

    @Column(name = "sky", nullable = false)
    private Integer sky;

    @Column(name = "prep", nullable = false)
    private Date prep;

    @OneToMany(mappedBy = "weather")
    private List<Board> boards;
}
