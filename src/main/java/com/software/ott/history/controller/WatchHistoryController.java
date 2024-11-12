package com.software.ott.history.controller;

import com.software.ott.common.dto.StringTypeMessageResponse;
import com.software.ott.history.dto.WatchHistoryResponse;
import com.software.ott.history.service.WatchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/watch")
@Tag(name = "시청 기록", description = "시청 기록 관련 API")
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    @Operation(summary = "시청 기록에 저장", description = "사용자가 컨텐츠를 시청 기록에 저장합니다.")
    @PostMapping("/{contentId}")
    public ResponseEntity<StringTypeMessageResponse> saveWatchHistory(@RequestAttribute("memberId") Long memberId, @PathVariable("contentId") Long contentId) {
        watchHistoryService.saveWatchHistory(memberId, contentId);
        return ResponseEntity.ok().body(new StringTypeMessageResponse("시청 기록에 저장되었습니다."));
    }

    @Operation(summary = "사용자의 시청기록 조회", description = "사용자의 모든 시청 기록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<WatchHistoryResponse>> readAllWatchHistory(@RequestAttribute("memberId") Long memberId) {
        List<WatchHistoryResponse> watchHistoryResponses = watchHistoryService.readAllWatchHistoryContentsByMember(memberId);
        return ResponseEntity.ok().body(watchHistoryResponses);
    }

    @Operation(summary = "사용자의 시청기록 삭제", description = "사용자의 시청 기록을 직접 삭제합니다.")
    @DeleteMapping("/{contentId}")
    public ResponseEntity<StringTypeMessageResponse> deleteWatchHistory(@RequestAttribute("memberId") Long memberId, @PathVariable("contentId") Long contentId) {
        watchHistoryService.deleteWatchHistory(memberId, contentId);
        return ResponseEntity.ok().body(new StringTypeMessageResponse("시청 기록이 삭제되었습니다."));
    }
}
