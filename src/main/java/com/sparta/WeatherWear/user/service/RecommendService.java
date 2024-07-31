package com.sparta.WeatherWear.user.service;

import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.global.dto.ResponseDTO;
import com.sparta.WeatherWear.board.dto.BoardResponseDTO;
import com.sparta.WeatherWear.clothes.dto.ClothesResponseDTO;
import com.sparta.WeatherWear.weather.service.WeatherService;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.weather.entity.Weather;
import com.sparta.WeatherWear.wishlist.dto.NaverProductResponseDTO;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.board.repository.BoardRepository;
import com.sparta.WeatherWear.clothes.repository.ClothesRepository;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.wishlist.service.NaverShoppingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RecommendService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final WeatherService weatherService;
    private final NaverShoppingService naverShoppingService;
    private final ClothesRepository clothesRepository;
    private final BoardRepository boardRepository;

    /* 기온에 맞는 옷 목록 리스트 */
    private static final Map<Double, List<ClothesType>> temperatureClothesMap = new LinkedHashMap<>();
    static {
        temperatureClothesMap.put(28.0, Arrays.asList(ClothesType.SLEEVELESS, ClothesType.SHORT_SLEEVE, ClothesType.SHORTS, ClothesType.DRESS));
        temperatureClothesMap.put(23.0, Arrays.asList(ClothesType.SHORT_SLEEVE, ClothesType.LIGHT_SHIRT, ClothesType.SHORTS, ClothesType.COTTON_PANTS));
        temperatureClothesMap.put(20.0, Arrays.asList(ClothesType.CARDIGAN, ClothesType.LONG_SLEEVE, ClothesType.COTTON_PANTS, ClothesType.JEANS));
        temperatureClothesMap.put(17.0, Arrays.asList(ClothesType.LIGHT_KNIT, ClothesType.SWEATSHIRT, ClothesType.CARDIGAN, ClothesType.JEANS));
        temperatureClothesMap.put(12.0, Arrays.asList(ClothesType.JACKET, ClothesType.CARDIGAN, ClothesType.MILITARY_JACKET, ClothesType.STOCKINGS, ClothesType.JEANS, ClothesType.COTTON_PANTS));
        temperatureClothesMap.put(9.0, Arrays.asList(ClothesType.JACKET, ClothesType.TRENCH_COAT, ClothesType.MILITARY_JACKET, ClothesType.KNIT, ClothesType.JEANS, ClothesType.STOCKINGS));
        temperatureClothesMap.put(5.0, Arrays.asList(ClothesType.COAT, ClothesType.LEATHER_JACKET, ClothesType.HEAT_TECH, ClothesType.KNIT, ClothesType.LEGGINGS));
        temperatureClothesMap.put(Double.MIN_VALUE, Arrays.asList(ClothesType.PADDED_COAT, ClothesType.COAT, ClothesType.SCARF, ClothesType.LINED_CLOTHING));
    }

    public RecommendService(WeatherService weatherService, NaverShoppingService naverShoppingService, ClothesRepository clothesRepository, BoardRepository boardRepository) {
        this.weatherService = weatherService;
        this.naverShoppingService = naverShoppingService;
        this.clothesRepository = clothesRepository;
        this.boardRepository = boardRepository;
    }

    /*______________________Recommend___________________*/

    /* 모든 추천 아이템 불러오기 */
    public List<List<? extends ResponseDTO>> getRecommends (UserDetailsImpl userDetails, long id){
        //반환을 위한 배열의 배열 선언
        List<List<? extends ResponseDTO> > recommendResponseDTOS = new ArrayList<>();
        // 날씨값 찾기
        Weather weather = weatherService.getWeatherByAddress(id);
        User user = userDetails.getUser();

        /* 1. 날씨 기반 옷차림 추천 */
        recommendResponseDTOS.add(getClothesByWeather(user, weather));

        /* 2. 내 옷차림 추천 : 내 게시물 / 현재 장소와 시간의 날씨와 유사한  */
        recommendResponseDTOS.add(getBoardsByMyBoards(user,weather));

        /* 3. 트랜드 옷차림 추천 */
        recommendResponseDTOS.add(getBoardsByTrends(user, weather));

        /* 4. 네이버 아이템 추천 */
        recommendResponseDTOS.add(getNaverProductsByNaver(user, weather));


        return recommendResponseDTOS;
    }

    /* 1. 날씨 기반 옷차림 추천 : 내 옷장 속 옷 아이템 추천 */
    @Transactional(readOnly = true)
    public List<? extends ResponseDTO>  getClothesByWeather(User user, Weather weather){
        /* 기온에 맞는 옷 타입 선정을 위한 배열 선언*/
        List<ClothesType> types = new ArrayList<>();
        for (Map.Entry<Double, List<ClothesType>> entry : temperatureClothesMap.entrySet()) {
            if (weather.getTMP() > entry.getKey()) { types = entry.getValue(); break; }
        }
        /* 사용자의 옷 중에 배열의 태그와 동일한 옷을 추천합니다. */
        return clothesRepository.findByUserAndTypeIn(user, types).stream().map(ClothesResponseDTO::new).toList();
    }

    /* 2. 나의 Best OOTD 추천 : 높은 좋아요의 게시물 추천 */
    @Transactional(readOnly = true)
    private List<? extends ResponseDTO>  getBoardsByMyBoards(User user,Weather weather) {
        // 온도 차이
        int tmpGap = 3;
        // 날씨 조건이 동일한 게시물의 목록을 불러옵니다.
        return boardRepository.findByUserAndWeather_SKYAndWeather_PTYAndWeather_TMPBetween(user, weather.getSKY(), weather.getPTY(), weather.getTMP() - tmpGap, weather.getTMP() + tmpGap)
                // 찾은 게시물의 좋아요 수를 기반으로 상위 5개를 선택합니다.
                .stream().sorted(Comparator.comparingInt(Board::getLikesSize).reversed()).limit(5).map(BoardResponseDTO::new).toList();
    }

    /* 3. 트랜드 OOTD : 높은 좋아요의 게시물 추천 */
    @Transactional(readOnly = true)
    private List<? extends ResponseDTO>  getBoardsByTrends(User user,Weather weather){
        int tmpGap = 3;
        // 현재 시간과 날씨값이 동일한 게시물 목록을 찾습니다. 사용자의 게시물은 제외합니다.
        List<Board> boards = boardRepository.findByWeather_SKYAndWeather_PTYAndWeather_TMPBetween(weather.getSKY(),weather.getPTY(),weather.getTMP()-tmpGap,weather.getTMP()+tmpGap).stream().filter(board -> !board.getUser().equals(user)).toList();
        // 좋아요 수가 높은 상위 3개
        List<Board> topLikesBoards = boards.stream().sorted(Comparator.comparingInt(Board::getLikesSize).reversed()).limit(3).toList();
        // 좋아요 수 높은 게시물을 제외한 나머지 목록
        List<Board> remainingBoards = boards.stream().filter(board -> !topLikesBoards.contains(board)).toList();
        // 조회 수가 높은 상위 3개
        List<Board> topViewsBoards = remainingBoards.stream().sorted(Comparator.comparingInt(Board::getViews).reversed()).limit(3).toList();
        // 조회 수 높은 게시물을 제외한 나머지 목록
        remainingBoards = remainingBoards.stream().filter(board -> !topViewsBoards.contains(board)).toList();
        // 댓글 수가 높은 상위 3개
        List<Board> topCommentsBoards = remainingBoards.stream().sorted(Comparator.comparingInt(Board::getCommentsSize).reversed()).limit(3).toList();

        // 결과를 배열에 저장하고 반환합니다.
        List<ResponseDTO> response = new ArrayList<>();
        for (Board board : topLikesBoards) response.add(new BoardResponseDTO(board));
        for (Board board : topViewsBoards) response.add(new BoardResponseDTO(board));
        for (Board board : topCommentsBoards) response.add(new BoardResponseDTO(board));
        return response;
    }

    /* 4. 네이버에서 아이템들을 추천해줍니다. */
    @Transactional(readOnly = true)
    private List<? extends ResponseDTO>  getNaverProductsByNaver(User user, Weather weather){
        logger.info("네이버 아이템 추천 받기");
        /* 기온에 맞는 옷 타입 선정을 위한 배열 선언*/
        List<ClothesType> types = new ArrayList<>();
        for (Map.Entry<Double, List<ClothesType>> entry : temperatureClothesMap.entrySet()) {
            if (weather.getTMP() > entry.getKey()) { types = entry.getValue(); break; }
        }
        // 결과를 배열에 저장하고 반환합니다.
        List<ResponseDTO> response = new ArrayList<>();
        for (ClothesType type : types) {
            String query = type.toString() + " " + user.getGender();  // ClothesType에서 쿼리 문자열 변환
            logger.info(query);
            List<NaverProductResponseDTO> naverProducts = naverShoppingService.searchProducts(query, 3);
            response.addAll(naverProducts);
        }
        return response;
    }
}
