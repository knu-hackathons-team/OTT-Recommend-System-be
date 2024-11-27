package com.software.ott.friend.dto;

import com.software.ott.content.entity.Content;

public record FriendRecommendResponse(
        Long RecommendId,
        String senderName,
        String senderEmail,
        Content content,
        String reason
) {
}
