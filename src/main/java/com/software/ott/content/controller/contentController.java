package com.software.ott.content.controller;

import com.software.ott.content.dto.ContentResponse;
import com.software.ott.content.service.ContentService;
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
@RequestMapping("/api/search")
@Tag(name = "검색", description = "검색 관련 API")
public class contentController {

    private final ContentService contentService;

    @Operation(summary = "title 기반 검색", description = "title 기반으로 검색합니다.")
    @GetMapping("/title/{title}")
    public ResponseEntity<List<ContentResponse>> readByTitle(@PathVariable String title) {
        List<ContentResponse> contentResponses = contentService.readContentsWithTitle(title);
        return ResponseEntity.ok().body(contentResponses);
    }

    @Operation(summary = "genre 기반 검색", description = "genre 기반으로 검색합니다.")
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<ContentResponse>> readByGenre(@PathVariable String genre) {
        List<ContentResponse> contentResponses = contentService.readContentsWithGenre(genre);
        return ResponseEntity.ok().body(contentResponses);
    }

    @Operation(summary = "cast 기반 검색", description = "cast 기반으로 검색합니다.")
    @GetMapping("/cast/{cast}")
    public ResponseEntity<List<ContentResponse>> readByCast(@PathVariable String cast) {
        List<ContentResponse> contentResponses = contentService.readContentsWithCast(cast);
        return ResponseEntity.ok().body(contentResponses);
    }
}
