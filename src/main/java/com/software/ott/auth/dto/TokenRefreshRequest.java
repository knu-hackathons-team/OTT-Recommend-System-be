package com.software.ott.auth.dto;

public record TokenRefreshRequest(
        String refreshToken
) {
}
