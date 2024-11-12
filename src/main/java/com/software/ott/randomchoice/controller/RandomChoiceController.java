package com.software.ott.randomchoice.controller;

import com.software.ott.randomchoice.dto.RandomContentResponse;
import com.software.ott.randomchoice.service.RandomChoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/random")
@Tag(name = "초기 선택", description = "초기 컨텐츠 선택 관련 API")
public class RandomChoiceController {

    private final RandomChoiceService randomChoiceService;

    @Operation(summary = "랜덤한 컨텐츠 추출", description = "랜덤한 count개의 컨텐츠를 추출하여 리스트형태로 반환합니다.")
    @GetMapping("/{count}")
    public ResponseEntity<List<RandomContentResponse>> getRandomContentList(@PathVariable("count") int count) {
        List<RandomContentResponse> randomContentResponses = randomChoiceService.getRandomContentList(count);
        return ResponseEntity.ok().body(randomContentResponses);
    }
}
