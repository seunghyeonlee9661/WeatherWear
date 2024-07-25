package com.sparta.WeatherWear.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.entity.Address;
import com.sparta.WeatherWear.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class KakaoMapService {
    private static final Logger logger = Logger.getLogger(KakaoMapService.class.getName());

    private static final String KAKAO_GEOCODING_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=%s&y=%s";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final AddressRepository addressRepository; // AddressRepository 주입

    @Value("${kakao.client.id}")
    private String kakaoApiKey;  // 카카오 API 키를 설정 파일에서 읽어옵니다.

    public KakaoMapService(RestTemplate restTemplate, ObjectMapper objectMapper, AddressRepository addressRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.addressRepository = addressRepository;
    }

    public Address findAddressByCoordinate(double x, double y) throws JsonProcessingException {
        String url = String.format(KAKAO_GEOCODING_URL, x, y);
        logger.info(url);


        // 헤더에 API 키를 추가합니다.
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 요청을 보냅니다.
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String responseString = response.getBody();
        logger.info(responseString);

        if (responseString == null || responseString.isEmpty()) {
            throw new RuntimeException("No data received from Kakao API.");
        }

        // JSON 응답을 처리합니다.
        JsonNode rootNode = objectMapper.readTree(responseString);
        String city = rootNode.path("documents").path(0).path("region_1depth_name").asText();
        String county = rootNode.path("documents").path(0).path("region_2depth_name").asText();
        String district = rootNode.path("documents").path(0).path("region_3depth_name").asText();

        // 데이터베이스에서 행정구역 코드 검색
        Optional<Address> address = addressRepository.findByCityAndCountyAndDistrict(city, county, district);
        if(address.isEmpty()){
            address = addressRepository.findByCityAndCountyAndDistrict(city, county, null);
        }
        return address.get();
    }
}
