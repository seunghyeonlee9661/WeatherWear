package com.sparta.WeatherWear.wishlist.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.wishlist.dto.NaverProductRequestDTO;
import com.sparta.WeatherWear.wishlist.dto.NaverProductResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NaverShoppingService {

    private static final Logger log = LoggerFactory.getLogger(NaverShoppingService.class);
    @Value("${naver.client.id}")
    private String clientId;
    @Value("${naver.client.secret}")
    private String clientSecret;

    // 네이버로부터 입력에 대한 검색 결과를 받아오는 기능
    public List<NaverProductResponseDTO> searchProducts(String query, int display){
        /* 헤더 정의 */
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        /* url 정의 */
        String apiUrl = "https://openapi.naver.com/v1/search/shop.json?query=" + query+ "&display=" + display + "&start=" + 1 + "&sort=" + "sim";
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
                log.info(itemsJson);

                // JSON 배열을 List<NaverProductRequestDTO>로 변환
                return objectMapper.readValue(itemsJson, new TypeReference<List<NaverProductRequestDTO>>() {})
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
