package com.software.ott.history.controller;

import com.software.ott.common.dto.StringTypeMessageResponse;
import com.software.ott.history.dto.ContentLikeResponse;
import com.software.ott.history.dto.TopContentLikeResponse;
import com.software.ott.history.service.ContentLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
@Tag(name = "좋아요/싫어요", description = "컨텐츠 좋아요 싫어요 관련 API")
public class ContentLikeController {

    private final ContentLikeService contentLikeService;

    @Operation(summary = "좋아요/싫어요 선택", description = "컨텐츠의 좋아요/싫어요를 표시합니다.")
    @PostMapping("/{contentId}/{like}")
    public ResponseEntity<StringTypeMessageResponse> selectContentLike(@RequestAttribute("memberId") Long memberId, @PathVariable("contentId") Long contentId, @PathVariable("like") boolean like) {
        contentLikeService.selectLikeDislike(memberId, contentId, like);
        return ResponseEntity.ok().body(new StringTypeMessageResponse("선택되었습니다."));
    }

    @Operation(summary = "좋아요/싫어요한 컨텐츠 보기", description = "사용자가 좋아요/싫어요한 컨텐츠를 확인합니다")
    @GetMapping("/{like}")
    public ResponseEntity<List<ContentLikeResponse>> readAllContentsLikeByMember(@RequestAttribute("memberId") Long memberId, @PathVariable("like") boolean like) {
        List<ContentLikeResponse> contentLikeResponses = contentLikeService.readAllLikeContentsByMember(memberId, like);
        return ResponseEntity.ok().body(contentLikeResponses);
    }

    @Operation(summary = "사용자들 좋아요한 컨텐츠 TOP10", description = "사용자들의 좋아요한 컨텐츠들 중 TOP 10을 조회합니다.")
    @GetMapping("/top")
    public ResponseEntity<List<TopContentLikeResponse>> getTop10MostLikedContents() {
        List<TopContentLikeResponse> topContentLikeResponses = contentLikeService.getTop10MostLikedContents();
        return ResponseEntity.ok().body(topContentLikeResponses);
    }

    @Operation(summary = "선호도 표시 삭제", description = "사용자의 선호도 표시를 삭제합니다.")
    @DeleteMapping("/{contentId}")
    public ResponseEntity<StringTypeMessageResponse> deleteContentLike(@RequestAttribute("memberId") Long memberId, @PathVariable Long contentId) {
        contentLikeService.deleteContentLike(memberId, contentId);
        return ResponseEntity.ok().body(new StringTypeMessageResponse("삭제되었습니다."));
    }
}
