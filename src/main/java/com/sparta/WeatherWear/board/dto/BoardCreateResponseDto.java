package com.sparta.WeatherWear.board.dto;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardImage;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.board.time.Timestamped;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;

import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardCreateResponseDto  {

    private long id;
    // 사용자 정보
    //Fixme : 이 부분은 사용자 DTO로 포장 하시구요!
    private Long userId;
    private String nickname;
    private String image;
    //
    private String title;
    private String contents;
    private boolean isPrivate;
//    private LocalDateTime registDate;
//    private LocalDateTime updateDate;
    //
    //Fixme : Weather의 경우에도 필요한 값만 넣어서 DTO로 만드시면 됩니다!
    private Weather weather;
    private String address;
    //
    private int boardLikes;
    //
    private int commentsSize;
    //
    private List<ClothesRequestDTO> clothesRequestDTO;
    private List<String> boardImages;
    private int views;

    // 게시물 찾을 때
    public BoardCreateResponseDto(Board board) {
        this.id = board.getId();
        // 사용자
        this.userId = board.getUser().getId();
        this.nickname = board.getUser().getNickname();
        this.image = board.getUser().getImage();
        // 게시물
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
//        this.registDate = board.getRegistDate();
//        this.updateDate = board.getUpdateDate();
        // 날씨
        this.weather = board.getWeather();
        this.address = board.getAddress();
        //
        this.boardLikes = board.getLikesSize();
        // 보드의 태그 Response
        List<ClothesRequestDTO> requestDTOS = new ArrayList<>();
        for(BoardTag boardTag : board.getBoardTags()) {
             ClothesRequestDTO requestDTO = new ClothesRequestDTO(boardTag.getColor(),boardTag.getType());
             requestDTOS.add(requestDTO);
        }
        this.clothesRequestDTO = requestDTOS;
        //
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = board.getViews();
    }

    // 처음 생성할 때
    //Fixme : 게시물 생성에 대한 Response 빼시면 될거 같아요!
    public BoardCreateResponseDto(Board board, List<ClothesRequestDTO> clothesRequestDTO) {
        this.id = board.getId();
        // 사용자
        this.userId = board.getUser().getId();
        this.nickname = board.getUser().getNickname();
        this.image = board.getUser().getImage();
        // 게시물
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
//        this.registDate = board.getRegistDate();
//        this.updateDate = board.getUpdateDate();
        // 날씨
        this.weather = board.getWeather();
        this.address = board.getAddress();
        //
        this.boardLikes = board.getLikesSize();
//        this.commentsSize = board.getCommentsSize();

        //
        this.clothesRequestDTO = clothesRequestDTO;
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = board.getViews();
    }

    // 게시물 조회 & 조회수 추가
    //Fixme
    public BoardCreateResponseDto(Board board, int views) {
        this.id = board.getId();
        // 사용자
        this.userId = board.getUser().getId();
        this.nickname = board.getUser().getNickname();
        this.image = board.getUser().getImage();
        // 게시물
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
//        this.registDate = board.getRegistDate();
//        this.updateDate = board.getUpdateDate();
        // 날씨
        this.weather = board.getWeather();
        this.address = board.getAddress();
        //
        this.boardLikes = board.getLikesSize();
//        this.commentsSize = board.getCommentsSize();
        //
        // 보드의 태그 Response
        List<ClothesRequestDTO> requestDTOS = new ArrayList<>();
        for(BoardTag boardTag : board.getBoardTags()) {
            ClothesRequestDTO requestDTO = new ClothesRequestDTO(boardTag.getColor(),boardTag.getType());
            requestDTOS.add(requestDTO);
        }
        this.clothesRequestDTO = requestDTOS;
        //
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = views;
    }
}
