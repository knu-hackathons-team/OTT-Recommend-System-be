package com.software.ott.recommend.controller;

import com.software.ott.recommend.dto.RecommendResponse;
import com.software.ott.recommend.service.RecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
@Tag(name = "추천 컨텐츠", description = "추천 컨텐츠 관련 API")
public class RecommendController {

    private final RecommendService recommendService;

    @Operation(summary = "추천 컨텐츠 조회", description = "유저의 추천 컨텐츠를 count개수만큼 조회합니다.")
    @GetMapping("/{count}")
    public ResponseEntity<RecommendResponse> readRecommendContents(@RequestAttribute("memberId") Long memberId, @PathVariable int count) {
        RecommendResponse recommendResponse = recommendService.recommendContentsForMember(memberId, count);
        return ResponseEntity.ok().body(recommendResponse);
    }
}
