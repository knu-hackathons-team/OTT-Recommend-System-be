package com.software.ott.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {

}
