package com.sparta.WeatherWear.weather.service;

import com.sparta.WeatherWear.weather.entity.Address;
import com.sparta.WeatherWear.weather.entity.Weather;
import com.sparta.WeatherWear.weather.repository.AddressRepository;
import com.sparta.WeatherWear.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * 작성자 :
 * 날씨 정보 데이터를 받아서 처리합니다.
 * 출처 : https://www.data.go.kr/data/15084084/openapi.do
 */
@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${weather.api.key}") //기상청 api 키
    private String serviceKey;
    @Value("${weather.api.uri}") // 기상청 api base uri
    private String weatherURi;

    private final WeatherRepository weatherRepository;
    private final AddressRepository addressRepository;

    /* 법정동 코드와 현재 시간으로 DB에서 날씨 정보를 찾아 반환합니다. */
    @Transactional
    @Cacheable(value = "weather", key = "#id")
    public Weather getWeatherByAddress(Long id){
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS); /* 현재 시간에 대해 분 값을 제외하여 확인합니다. (1시 24분 -> 1시) */

        return weatherRepository.findByAddressAndDate(address, Date.from(now.plusHours(1).atZone(ZoneId.systemDefault()).toInstant()))  // Weather 데이터를 조회하고 반환합니다.
                // Weather가 없는 경우 기상청 API에 날씨 정보를 요청합니다.
                .orElseGet(() -> {
                    Weather newWeather = null;
                    try {
                        // 기상청 API에 날씨 값을 요청합니다.
                        newWeather = getWeatherByAPI(address, now);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // 새로운 날씨 값을 서버에 저장합니다.
                    weatherRepository.save(newWeather);
                    return newWeather;
                });
    }

    /* 위치와 날짜를 기반으로 API를 요청하고 날씨 정보를 받아옵니다. */
    private Weather getWeatherByAPI(Address address, LocalDateTime date) throws IOException {
        /* 현재 시간 값에 따른 API 요청 시간을 생성합니다.*/
        /* 단기 예보의 경우, 2시부터 3시간 간격으로 데이터를 생성합니다. 예보는 생성 시간 기준 +6시간이 생성됩니다. */
        String baseDate;
        String baseTime;
        /* 현재 시간이 2보다 작을 경우 전날 23시 */
        if (date.getHour() < 2) {
            baseDate = date.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            baseTime = "2300";
            /* 시간을 3시간 단위로 조절합니다. */
        } else {
            baseDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            int hour = date.getHour();
            int adjustedHour = (hour - 2) % 24; // 2시부터 시작, 24시간 범위로 조정
            adjustedHour = (adjustedHour + 24) % 24; // 음수 시간 보정
            int baseTimeHour = (adjustedHour / 3) * 3 + 2; // 2시부터 3시간 간격
            // baseTimeHour가 24시를 넘는 경우, 24시에서 빼줍니다.
            if (baseTimeHour >= 24) {
                baseTimeHour -= 24;
            }
            baseTime = String.format("%02d00", baseTimeHour);
        }

        try {
            /* URL 생성*/
            String urlBuilder = weatherURi +
                    "?serviceKey=" + serviceKey + /* <- URLEncoder에 키값을 넣으면 변환 발생으로 직접 입력 */
                    "&pageNo=" + URLEncoder.encode("1", StandardCharsets.UTF_8) +
                    "&numOfRows=" + URLEncoder.encode("100", StandardCharsets.UTF_8) +
                    "&dataType=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) +
                    "&base_date=" + URLEncoder.encode(baseDate, StandardCharsets.UTF_8) +
                    "&base_time=" + URLEncoder.encode(baseTime, StandardCharsets.UTF_8) +
                    "&nx=" + URLEncoder.encode(String.valueOf(address.getNx()), StandardCharsets.UTF_8) +
                    "&ny=" + URLEncoder.encode(String.valueOf(address.getNy()), StandardCharsets.UTF_8);

            /* JSONObject를 처리해서 아이템 목록을 가져옵니다.*/
            JSONObject jsonResponse = getJsonObject(urlBuilder);
            JSONArray items = jsonResponse.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");

            /* 카테고리와 값들을 찾아내서 저장할 Map 선언 */
            Map<String, Double> weatherData = new HashMap<>();

            /* 요청은 현재 시간 기준 다음 정각 (13시 42분 -> 14시)을 기준으로 날씨 값을 찾아 비교합니다. */
            LocalDateTime compareDate = date.plusHours(1);
            String compareDateStr = compareDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String compareTimeStr = compareDate.format(DateTimeFormatter.ofPattern("HHmm"));


            /* 아이템 목록에서 필요한 데이터만 찾아서 Map에 저장 */
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String fcstDate = item.getString("fcstDate");
                String fcstTime = item.getString("fcstTime");
                if (compareDateStr.equals(fcstDate) && compareTimeStr.equals(fcstTime)) {
                    String category = item.getString("category");
                    Double fcstValue = item.optDouble("fcstValue",0.0);
                    weatherData.put(category, fcstValue);
                }
            }
            // Weather 객체에 저장하고 반환합니다.
            return new Weather(compareDateStr,compareTimeStr,address,
                    weatherData.getOrDefault("POP", null),
                    (int)Math.round(weatherData.getOrDefault("PTY", null)),
                    weatherData.getOrDefault("PCP", null),
                    weatherData.getOrDefault("REH", null),
                    weatherData.getOrDefault("SNO", null),
                    (int)Math.round(weatherData.getOrDefault("SKY", null)),
                    weatherData.getOrDefault("TMP", null),
                    weatherData.getOrDefault("TMN", null),
                    weatherData.getOrDefault("TMX", null),
                    weatherData.getOrDefault("UUU", null),
                    weatherData.getOrDefault("VVV", null),
                    weatherData.getOrDefault("WAV", null),
                    weatherData.getOrDefault("VEC", null),
                    weatherData.getOrDefault("WSD", null));
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
