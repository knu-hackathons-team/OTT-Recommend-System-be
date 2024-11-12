package com.software.ott.history.dto;

import com.software.ott.content.entity.Content;

import java.time.LocalDateTime;

public record WatchHistoryResponse(
        LocalDateTime watchedDateTime,
        Content content
) {
}
