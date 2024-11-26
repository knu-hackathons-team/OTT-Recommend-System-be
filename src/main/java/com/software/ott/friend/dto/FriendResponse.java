package com.software.ott.friend.dto;

public record FriendResponse(
        Long friendRequestId,
        String friendName,
        String friendEmail
) {
}
