package com.sparta.WeatherWear.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.board.entity.BoardImage;
import com.sparta.WeatherWear.board.entity.BoardTag;
import com.sparta.WeatherWear.clothes.dto.ClothesRequestDTO;

import com.sparta.WeatherWear.user.dto.UserSimpleDto;
import com.sparta.WeatherWear.weather.entity.Weather;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardCreateResponseDto  {

    private long id;
    // 사용자 정보
    private UserSimpleDto userSimpleDto = new UserSimpleDto();

    //
    private String title;
    private String contents;
    private boolean isPrivate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    //
//    Fixme : Weather의 경우에도 필요한 값만 넣어서 DTO로 만드시면 됩니다!
    private Weather weather;
    private String address;
    //
    private int boardLikesCount;
    //
//    private int commentsSize;
    //
    private List<ClothesRequestDTO> tegs;
    private List<String> boardImages;
    private int views;

    // 게시물 찾을 때
    public BoardCreateResponseDto(Board board) {
        this.id = board.getId();
        // 사용자
        this.userSimpleDto.setUserId(board.getUser().getId());
        this.userSimpleDto.setNickname(board.getUser().getNickname());
        this.userSimpleDto.setImage(board.getUser().getImage());
        // 게시물
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
        //시간
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        // 날씨
        this.weather = board.getWeather();
        this.address = board.getAddress();
        //
        this.boardLikesCount = board.getLikesSize();
        // 보드의 태그 Response
        List<ClothesRequestDTO> requestDTOS = new ArrayList<>();
        for(BoardTag boardTag : board.getBoardTags()) {
             ClothesRequestDTO requestDTO = new ClothesRequestDTO(boardTag.getColor(),boardTag.getType());
             requestDTOS.add(requestDTO);
        }
        this.tegs = requestDTOS;
        //
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = board.getViews();
    }

    // 처음 생성할 때
    //Fixme : 게시물 생성에 대한 Response 빼시면 될거 같아요!
    public BoardCreateResponseDto(Board board, List<ClothesRequestDTO> clothesRequestDTO) {
        this.id = board.getId();
        // 사용자
        this.userSimpleDto.setUserId(board.getUser().getId());
        this.userSimpleDto.setNickname(board.getUser().getNickname());
        this.userSimpleDto.setImage(board.getUser().getImage());
        // 게시물
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
        //시간
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        // 날씨
        this.weather = board.getWeather();
        this.address = board.getAddress();
        //
        this.boardLikesCount = board.getLikesSize();
       //
        this.tegs = clothesRequestDTO;
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = board.getViews();
    }

    // 게시물 조회 & 조회수 추가
    //Fixme
    public BoardCreateResponseDto(Board board, int views) {
        this.id = board.getId();
        // 사용자
        this.userSimpleDto.setUserId(board.getUser().getId());
        this.userSimpleDto.setNickname(board.getUser().getNickname());
        this.userSimpleDto.setImage(board.getUser().getImage());
        // 게시물
        this.title = board.getTitle();
        this.contents = board.getContent();
        this.isPrivate = board.isPrivate();
        //시간
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        // 날씨
        this.weather = board.getWeather();
        this.address = board.getAddress();
        //
        this.boardLikesCount = board.getLikesSize();
        //
        // 보드의 태그 Response
        List<ClothesRequestDTO> requestDTOS = new ArrayList<>();
        for(BoardTag boardTag : board.getBoardTags()) {
            ClothesRequestDTO requestDTO = new ClothesRequestDTO(boardTag.getColor(),boardTag.getType());
            requestDTOS.add(requestDTO);
        }
        this.tegs = requestDTOS;
        //
        this.boardImages = board.getBoardImages().stream().map(BoardImage::getImagePath).toList(); // 경로만 가져오기
        this.views = views;
    }
}
