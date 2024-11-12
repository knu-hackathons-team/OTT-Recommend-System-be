package com.software.ott.recommend.dto;

import com.software.ott.content.entity.Content;

public record ContentRecommendation(
        Content content,
        String reason
) {
}
