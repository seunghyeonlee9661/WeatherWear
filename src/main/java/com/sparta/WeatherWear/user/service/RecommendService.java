package com.sparta.WeatherWear.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.WeatherWear.board.entity.Board;
import com.sparta.WeatherWear.clothes.entity.Clothes;
import com.sparta.WeatherWear.global.dto.ResponseDTO;
import com.sparta.WeatherWear.user.dto.RecommendBoardResponseDTO;
import com.sparta.WeatherWear.clothes.dto.ClothesResponseDTO;
import com.sparta.WeatherWear.weather.repository.WeatherRepository;
import com.sparta.WeatherWear.weather.service.WeatherService;
import com.sparta.WeatherWear.user.entity.User;
import com.sparta.WeatherWear.weather.entity.Weather;
import com.sparta.WeatherWear.wishlist.dto.NaverProductResponseDTO;
import com.sparta.WeatherWear.clothes.enums.ClothesType;
import com.sparta.WeatherWear.board.repository.BoardRepository;
import com.sparta.WeatherWear.clothes.repository.ClothesRepository;
import com.sparta.WeatherWear.global.security.UserDetailsImpl;
import com.sparta.WeatherWear.wishlist.entity.Wishlist;
import com.sparta.WeatherWear.wishlist.repository.WishlistRepository;
import com.sparta.WeatherWear.wishlist.service.NaverShoppingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final WeatherRepository weatherRepository;
    private final NaverShoppingService naverShoppingService;
    private final ClothesRepository clothesRepository;
    private final BoardRepository boardRepository;
    private final WishlistRepository wishlistRepository;

    /* 기온에 맞는 옷 목록 리스트 */
    private static final Map<Double, List<ClothesType>> temperatureClothesMap = new LinkedHashMap<>();
    static {
        temperatureClothesMap.put(28.0, Arrays.asList(ClothesType.SLEEVELESS, ClothesType.SHORT_SLEEVE, ClothesType.SHORTS, ClothesType.DRESS, ClothesType.MINI_SKIRT));
        temperatureClothesMap.put(23.0, Arrays.asList(ClothesType.SHORT_SLEEVE, ClothesType.LIGHT_SHIRT, ClothesType.SHORTS, ClothesType.LONG_SLEEVE));
        temperatureClothesMap.put(20.0, Arrays.asList(ClothesType.LONG_SLEEVE, ClothesType.LEGGINGS, ClothesType.LIGHT_KNIT, ClothesType.SLACKS, ClothesType.BLOUSE));
        temperatureClothesMap.put(17.0, Arrays.asList(ClothesType.KNIT, ClothesType.HOODIE, ClothesType.SWEAT_SHIRT, ClothesType.COTTON_PANTS, ClothesType.JEANS));
        temperatureClothesMap.put(12.0, Arrays.asList(ClothesType.JACKET, ClothesType.CARDIGAN, ClothesType.SHIRT));
        temperatureClothesMap.put(9.0, Arrays.asList(ClothesType.TRENCH_COAT, ClothesType.MILITARY_JACKET,ClothesType.STOCKINGS));
        temperatureClothesMap.put(5.0, Arrays.asList(ClothesType.COAT, ClothesType.LEATHER_JACKET,  ClothesType.LINED_CLOTHING));
        temperatureClothesMap.put(Double.MIN_VALUE, Arrays.asList(ClothesType.PADDED_COAT, ClothesType.SCARF, ClothesType.LINED_CLOTHING,ClothesType.HEAT_TECH));
    }

    public RecommendService(WeatherRepository weatherRepository, NaverShoppingService naverShoppingService, ClothesRepository clothesRepository, BoardRepository boardRepository, WishlistRepository wishlistRepository) {
        this.weatherRepository = weatherRepository;
        this.naverShoppingService = naverShoppingService;
        this.clothesRepository = clothesRepository;
        this.boardRepository = boardRepository;
        this.wishlistRepository = wishlistRepository;
    }

    /*______________________Recommend___________________*/

    /* 모든 추천 아이템 불러오기 */
    public List<List<? extends ResponseDTO>> getRecommends (UserDetailsImpl userDetails, long id) throws JsonProcessingException {
        //반환을 위한 배열의 배열 선언
        List<List<? extends ResponseDTO> > recommendResponseDTOS = new ArrayList<>();

        // 날씨값 찾기
        Weather weather = weatherRepository.getWeatherById(id).orElseThrow(()-> new IllegalArgumentException("날씨 ID가 올바르지 않습니다."));

        // 로그인한 사용자일 경우 각 항목에 대해 모두 추천 리스트를 받는다.
        if(userDetails != null){
            User user = userDetails.getUser();
            /* 1. 날씨 기반 옷차림 추천 */
            recommendResponseDTOS.add(getClothesByWeather(user, weather));
            /* 2. 내 옷차림 추천 : 내 게시물 / 현재 장소와 시간의 날씨와 유사한  */
            recommendResponseDTOS.add(getBoardsByMyBoards(user,weather));
            /* 3. 트랜드 옷차림 추천 */
            recommendResponseDTOS.add(getBoardsByTrends(user, weather));
            /* 4. 네이버 아이템 추천 */
            recommendResponseDTOS.add(getNaverProductsByWeather(user, weather));
        }else{
            recommendResponseDTOS.add(getBoardsByTrends(null, weather));
        }
        return recommendResponseDTOS;
    }

    /* 1. 날씨 기반 옷차림 추천 : 내 옷장 속 옷 아이템 추천 */
    @Transactional(readOnly = true)
    public List<? extends ResponseDTO> getClothesByWeather(User user, Weather weather) {
        // 초기값 설정: 가장 높은 온도의 옷 목록으로 시작

        List<ClothesType> types = new ArrayList<>();
        // 온도 범위를 기준으로 리스트 선택
        for (Map.Entry<Double, List<ClothesType>> entry : temperatureClothesMap.entrySet()) {
            Double tempKey = entry.getKey();
            // 온도가 범위에 맞는 경우 목록을 선택
            if (weather.getTMP() >= tempKey) {
                types = entry.getValue(); // 온도에 맞는 목록으로 업데이트
                break;
            }
        }
        // 사용자의 옷 중에 배열의 태그와 동일한 옷을 추천합니다.
        List<Clothes> clothes = clothesRepository.findByUserAndTypeIn(user, types);
        return clothes.stream().map(ClothesResponseDTO::new).toList();
    }


    @Transactional(readOnly = true)
    protected List<? extends ResponseDTO> getBoardsByMyBoards(User user, Weather weather) {
        // 온도 차이
        int tmpGap = 3;
        // 날씨 조건이 동일한 게시물의 목록을 불러옵니다.
        List<Board> topBoards = boardRepository.findTopBoardsByUserAndWeatherWithScore(user.getId(), weather.getSKY(), weather.getPTY(), weather.getTMP() - tmpGap, weather.getTMP() + tmpGap);
        // 결과 반환
        return topBoards.stream().map(RecommendBoardResponseDTO::new).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    protected List<? extends ResponseDTO> getBoardsByTrends(User user, Weather weather) {
        int tmpGap = 3;
        List<Board> topBoards = new ArrayList<>();
        if(user != null)
            // 현재 시간과 날씨값이 동일한 게시물 목록을 찾습니다. 사용자의 게시물은 제외합니다.
            topBoards = boardRepository.findTopBoardsByWeatherExcludingUserWithScore(weather.getSKY(), weather.getPTY(), weather.getTMP() - tmpGap, weather.getTMP() + tmpGap, user.getId());
        else
            topBoards = boardRepository.findTopBoardsByWeatherWithScore(weather.getSKY(), weather.getPTY(), weather.getTMP() - tmpGap, weather.getTMP() + tmpGap);
        // 결과를 배열에 저장하고 반환합니다.
        return topBoards.stream().map(RecommendBoardResponseDTO::new).collect(Collectors.toList());
    }


// 기존 방법 하나씩 뽑고 제외하기 : 성능 측정으로 사용 가능
//    /* 2. 나의 Best OOTD 추천 : 높은 좋아요의 게시물 추천 */
//    @Transactional(readOnly = true)
//    protected List<? extends ResponseDTO>  getBoardsByMyBoards(User user, Weather weather) {
//        // 온도 차이
//        int tmpGap = 3;
//        // 날씨 조건이 동일한 게시물의 목록을 불러옵니다.
//        return boardRepository.findByUserAndWeather_SKYAndWeather_PTYAndWeather_TMPBetween(user, weather.getSKY(), weather.getPTY(), weather.getTMP() - tmpGap, weather.getTMP() + tmpGap)
//                // 찾은 게시물의 좋아요 수를 기반으로 상위 5개를 선택합니다.
//                .stream().sorted(Comparator.comparingInt(Board::getLikesSize).reversed()).limit(5).map(RecommendBoardResponseDTO::new).toList();
//    }
//
//    /* 3. 트랜드 OOTD : 높은 좋아요의 게시물 추천 */
//    @Transactional(readOnly = true)
//    protected List<? extends ResponseDTO>  getBoardsByTrends(User user, Weather weather){
//        int tmpGap = 3;
//        // 현재 시간과 날씨값이 동일한 게시물 목록을 찾습니다. 사용자의 게시물은 제외합니다.
//        List<Board> boards = boardRepository.findByWeather_SKYAndWeather_PTYAndWeather_TMPBetween(weather.getSKY(),weather.getPTY(),weather.getTMP()-tmpGap,weather.getTMP()+tmpGap).stream().filter(board -> !board.getUser().equals(user)).toList();
//        // 좋아요 수가 높은 상위 3개
//        List<Board> topLikesBoards = boards.stream().sorted(Comparator.comparingInt(Board::getLikesSize).reversed()).limit(3).toList();
//        // 좋아요 수 높은 게시물을 제외한 나머지 목록
//        List<Board> remainingBoards = boards.stream().filter(board -> !topLikesBoards.contains(board)).toList();
//        // 조회 수가 높은 상위 3개
//        List<Board> topViewsBoards = remainingBoards.stream().sorted(Comparator.comparingInt(Board::getViews).reversed()).limit(3).toList();
//        // 조회 수 높은 게시물을 제외한 나머지 목록
//        remainingBoards = remainingBoards.stream().filter(board -> !topViewsBoards.contains(board)).toList();
//        // 댓글 수가 높은 상위 3개
//        List<Board> topCommentsBoards = remainingBoards.stream().sorted(Comparator.comparingInt(Board::getCommentsSize).reversed()).limit(3).toList();
//
//        // 결과를 배열에 저장하고 반환합니다.
//        List<ResponseDTO> response = new ArrayList<>();
//        for (Board board : topLikesBoards) response.add(new RecommendBoardResponseDTO(board));
//        for (Board board : topViewsBoards) response.add(new RecommendBoardResponseDTO(board));
//        for (Board board : topCommentsBoards) response.add(new RecommendBoardResponseDTO(board));
//        return response;
//    }

    /* 4. 네이버에서 아이템들을 추천해줍니다. */
    @Transactional(readOnly = true)
    private List<? extends ResponseDTO> getNaverProductsByWeather(User user, Weather weather) throws JsonProcessingException {

        /* 기온에 맞는 옷 타입 배열 선언*/
        List<ClothesType> types = new ArrayList<>();
        for (Map.Entry<Double, List<ClothesType>> entry : temperatureClothesMap.entrySet()) {
            if (weather.getTMP() > entry.getKey()) { types = entry.getValue(); break; }
        }
        /* 사용자가 가진 위시리스트 아이템 ID 목록 */
        List<Wishlist> userWishlists = wishlistRepository.findByUserId(user.getId());
        Set<Long> wishlistedProductIds = userWishlists.stream().map(wishlist -> wishlist.getProduct().getId()).collect(Collectors.toSet());

        // 결과 저장 배열 선언
        List<ResponseDTO> response = new ArrayList<>();

        for (ClothesType type : types) {

            // 네이버 API 검색어를 선정합니다 : 타입 + 성별
            String query = type.toString() + " " + user.getGender();

            // 검색 결과 저장을 위한 배열을 선언합니다.
            List<NaverProductResponseDTO> filteredProducts = new ArrayList<>();
            // 검색 결과 페이지 번호를 선언합니다.
            int start = 1;
            List<NaverProductResponseDTO> naverProducts;
            do {
                // 검색어를 입력하고 네이버 API에 검색 결과를 받아옵니다.
                naverProducts = naverShoppingService.getProducts(query,start);
                // 받아온 네이버 결과의 ID가 현재 위시리스트에 포함되어 있는지 확인합니다.
                for (NaverProductResponseDTO product : naverProducts) {
                    // 받아온 네이버 결과의 ID가 현재 위시리스트에 포함되어 있는지 확인합니다.
                    if (!wishlistedProductIds.contains(product.getProductId())) {
                        // 포함되어 있지 않다면 리스트에 저장합니다.
                        // 저장되는 아이템에 검색어를 타입으로 추가합니다.
                        product.setType(type.name());
                        filteredProducts.add(product);
                        // 저장된 검색 결과가 3개 이상이라면 비교를 중단합니다.
                        if (filteredProducts.size() >= 3) {
                            break;
                        }
                    }else{
                    }
                }
                // 다음 검색 결과 페이지를 요청합니다.
                start++;
            } while (filteredProducts.size() < 3 && !naverProducts.isEmpty());
            // 검색 결과를 저장합니다.
            response.addAll(filteredProducts);
        }
        return response;
    }

    /* 위시리스트 삭제 */
    @Transactional
    public ResponseEntity<String> removeWishlistByProductId(UserDetailsImpl userDetails, long product_id){
        Long userId = userDetails.getUser().getId();
        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(userId,product_id).orElseThrow(() -> new IllegalArgumentException("No Wishlist"));
        if(!wishlist.getUser().getId().equals(userId))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사용자의 위시리스트 아이템이 아닙니다.");
        wishlistRepository.delete(wishlist);
        return ResponseEntity.ok("Wishlist delete successfully");
    }
}
