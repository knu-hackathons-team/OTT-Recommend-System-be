package com.software.ott.history.dto;

import com.software.ott.content.entity.Content;

public record TopContentLikeResponse(
        Content content,
        Long likeCount
) {
}
