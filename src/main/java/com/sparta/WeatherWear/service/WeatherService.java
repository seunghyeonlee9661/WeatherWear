package com.sparta.WeatherWear.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.WeatherWear.entity.Address;
import com.sparta.WeatherWear.entity.Weather;
import com.sparta.WeatherWear.entity.WeatherNew;
import com.sparta.WeatherWear.repository.WeatherNewRepository;
import com.sparta.WeatherWear.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private static final Logger logger = Logger.getLogger(WeatherService.class.getName());

    @Value("${weather.api.key}")
    private String serviceKey;
    @Value("${weather.api.uri}")
    private String weatherURi;

    private final WeatherNewRepository weatherNewRepository;
    private final KakaoMapService kakaoMapService;

    /* 좌표를 통해 날씨 정보를 반환합니다. 기존에 가지고 있지 않은 날씨 정보는 새롭게 데이터에 저장됩니다. */
    @Transactional
    public ResponseEntity<WeatherNew> getWeatherByCoordinate(double x, double y) throws JsonProcessingException {
        // 좌표로 지역 정보를 불러옵니다.
        Address address = kakaoMapService.findAddressByCoordinate(x, y);
        WeatherNew weather = getWeatherByAddress(address);
        return ResponseEntity.ok(weather);
    }

    /* 주소와 현재 시간으로 날씨 정보를 반환합니다. */
    @Transactional
    public WeatherNew getWeatherByAddress(Address address){
        // 현재 시간을 분 단위로 내림 처리하여 가져옵니다.
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        // Weather 데이터를 조회하고 반환합니다.
        return weatherNewRepository.findByAddressAndDate(address,Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                // Weather가 없는 경우
                .orElseGet(() -> {
                    WeatherNew newWeather = null;
                    try {
                        // 기상청 API에 날씨 값을 요청합니다.
                        newWeather = getWeatherByAPI(address,now);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // 새로운 날씨 값을 서버에 저장합니다.
                    weatherNewRepository.save(newWeather);
                    return newWeather;
                });
    }

    /* 장소와 날짜를 기반으로 API를 요청하고 날씨 정보를 받아옵니다. */
    private WeatherNew getWeatherByAPI(Address address, LocalDateTime date) throws IOException {
        String baseDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = date.format(DateTimeFormatter.ofPattern("HHmm"));
        try {
            /* URL 생성*/
            String urlBuilder = weatherURi +
                    "?serviceKey=" + serviceKey +
                    "&pageNo=" + URLEncoder.encode("1", StandardCharsets.UTF_8) +
                    "&numOfRows=" + URLEncoder.encode("8", StandardCharsets.UTF_8) +
                    "&dataType=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) +
                    "&base_date=" + URLEncoder.encode(baseDate, StandardCharsets.UTF_8) +
                    "&base_time=" + URLEncoder.encode(baseTime, StandardCharsets.UTF_8) +
                    "&nx=" + URLEncoder.encode(String.valueOf(address.getNx()), StandardCharsets.UTF_8) +
                    "&ny=" + URLEncoder.encode(String.valueOf(address.getNy()), StandardCharsets.UTF_8);
            /* JSONObject */
            JSONObject jsonResponse = getJsonObject(urlBuilder);
            JSONArray items = jsonResponse.getJSONObject("response")
                    .getJSONObject("body")
                    .getJSONObject("items")
                    .getJSONArray("item");
            /* 카테고리와 값들을 찾아내서 Map에 저장 */
            Map<String, Double> weatherData = new HashMap<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String category = item.getString("category");
                Double obsrValue = item.optDouble("obsrValue", Double.NaN);
                weatherData.put(category, obsrValue);
            }
            // Weather 객체에 저장하고 반환
            return new WeatherNew(baseDate, baseTime,address,
                    weatherData.getOrDefault("PTY", Double.NaN),
                    weatherData.getOrDefault("REH", Double.NaN),
                    weatherData.getOrDefault("RN1", Double.NaN),
                    weatherData.getOrDefault("T1H", Double.NaN),
                    weatherData.getOrDefault("UUU", Double.NaN),
                    weatherData.getOrDefault("VEC", Double.NaN),
                    weatherData.getOrDefault("VVV", Double.NaN),
                    weatherData.getOrDefault("WSD", Double.NaN));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing weather API response", e);
        }
    }

    /* URL을 연결해서 받아온 값을 JSONObject로 처리하는 역할을 수행합니다. */
    private static JSONObject getJsonObject(String urlBuilder) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlBuilder).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300
                        ? conn.getInputStream() : conn.getErrorStream()));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
        }
        rd.close();
        conn.disconnect();
        return new JSONObject(response.toString());
    }
}
