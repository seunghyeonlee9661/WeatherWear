package com.sparta.WeatherWear.wishlist.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.global.service.RedisService;
import com.sparta.WeatherWear.wishlist.dto.WishlistRequestDTO;
import com.sparta.WeatherWear.wishlist.dto.NaverProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NaverShoppingService {

    private static final Logger log = LoggerFactory.getLogger(NaverShoppingService.class);
    private final RedisService redisService;

    @Value("${naver.client.id}")
    private String clientId;
    @Value("${naver.client.secret}")
    private String clientSecret;

    public List<NaverProductResponseDTO> getProducts(String query,int start) throws JsonProcessingException {
        /* Redis에서 검색 결과 조회 */
        String redisKey = "naverProducts:" + query + ":" + start;
        String cachedResult = redisService.get(RedisService.NAVER_SHOPPING_API_PREFIX, redisKey);
        List<NaverProductResponseDTO> result;
        if (cachedResult != null) {
            // 캐시된 결과가 있다면 이를 반환
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.readValue(cachedResult, new TypeReference<List<NaverProductResponseDTO>>() {});
        } else {
            result = searchProductsByAPI(query, start);
            // 여기 Redis에 저장하는 기능 추가
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResult = objectMapper.writeValueAsString(result); // 객체 리스트를 JSON 문자열로 직렬화
            redisService.save(RedisService.NAVER_SHOPPING_API_PREFIX, redisKey, jsonResult, RedisService.NAVER_SHOPPING_API_DURATION); // 1시간 동안 캐시 저장
        }
        return result ;
    }

    // 네이버로부터 입력에 대한 검색 결과를 받아오는 기능
    public List<NaverProductResponseDTO> searchProductsByAPI(String query, int start){
        /* 헤더 정의 */
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        /* url 정의 */
        String apiUrl = "https://openapi.naver.com/v1/search/shop.json?query=" + query+ "&display=" + 100 + "&start=" + start + "&sort=" + "sim";
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출
        ResponseEntity<String> response = new RestTemplate().exchange(apiUrl, HttpMethod.GET, entity, String.class);
        // JSON 응답을 처리하여 NaverProduct 리스트로 변환
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String responseBody = response.getBody();
                // JSON 응답에서 "items" 필드 추출
                String itemsJson = objectMapper.readTree(responseBody).get("items").toString();

                // JSON 배열을 List<NaverProductRequestDTO>로 변환
                return objectMapper.readValue(itemsJson, new TypeReference<List<WishlistRequestDTO>>() {})
                        .stream()
                        .map(NaverProductResponseDTO::new)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
