package com.sparta.WeatherWear.controller;

import com.sparta.WeatherWear.dto.ResponseDTO;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import com.sparta.WeatherWear.service.RecommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ResponseRestController {

    private final RecommandService recommandService;

    /* 추천 아이템들 불러오기 */
    @GetMapping("/recommend")
    public ResponseEntity<List<List<? extends ResponseDTO>>> getRecommend(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(recommandService.getRecommends(userDetails,id));
    }
}
