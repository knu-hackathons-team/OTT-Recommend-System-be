package com.software.ott.friend.dto;

public record FriendRequestResponse(
        Long friendRequestId,
        String requesterName,
        String requesterEmail,
        String accepterName,
        String accepterEmail
) {
}
