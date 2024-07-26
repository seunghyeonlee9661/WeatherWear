package com.sparta.WeatherWear.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/*
작성자 : 이승현
날씨 정보 Entity
 */
@Getter
@Entity
@NoArgsConstructor // 기본 생성자 추가
/* 현재 기존의 Weather 작업에 방해가 되지 않도록 테이블 이름을 변경했습니다. */
/* 해당 코드를 따라간다면 기존 Weather를 제거하고 Table 어노테이션을 삭제하시면 됩니다. */
/* 또한 DB에 날씨 데이터 테이블을 물리적으로 삭제하고 하이버네이트로 생성될 수 있도록 해주시면 됩니다. */
@Table(name="weather_new")
public class WeatherNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "pop")
    private Double POP;  // 강수확률

    @Column(name = "pty")
    private Double PTY;  // 강수형태

    @Column(name = "pcp")
    private Double PCP;  // 1시간 강수량

    @Column(name = "reh")
    private Double REH;  // 습도

    @Column(name = "sno")
    private Double SNO;  // 1 시간 신적설

    @Column(name = "sky")
    private Double SKY;  // 하늘상태

    @Column(name = "tmp")
    private Double TMP;  // 1 시간 기온

    @Column(name = "tmn")
    private Double TMN;  // 일 최저기온

    @Column(name = "tmx")
    private Double TMX;  // 일 최고기온

    @Column(name = "uuu")
    private Double UUU;  // 풍속(동서성분)

    @Column(name = "vvv")
    private Double VVV;  // 풍속(남북성분)

    @Column(name = "wav")
    private Double WAV;  // 파고

    @Column(name = "vec")
    private Double VEC;  // 풍향

    @Column(name = "wsd")
    private Double WSD;  // 풍속

    public WeatherNew(String baseDate, String baseTime, Address address, Double POP, Double PTY, Double PCP, Double REH, Double SNO, Double SKY, Double TMP, Double TMN, Double TMX, Double UUU, Double VVV, Double WAV, Double VEC, Double WSD) throws ParseException {
        this.POP = POP;
        this.PTY = PTY;
        this.PCP = PCP;
        this.REH = REH;
        this.SNO = SNO;
        this.SKY = SKY;
        this.TMP = TMP;
        this.TMN = TMN;
        this.TMX = TMX;
        this.UUU = UUU;
        this.VVV = VVV;
        this.WAV = WAV;
        this.VEC = VEC;
        this.WSD = WSD;
        this.date = new java.text.SimpleDateFormat("yyyyMMddHHmm").parse(baseDate + baseTime);
        this.address = address;
    }
}
