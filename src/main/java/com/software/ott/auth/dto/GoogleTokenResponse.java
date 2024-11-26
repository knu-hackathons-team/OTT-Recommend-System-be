package com.software.ott.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleTokenResponse(
        String accessToken,
        int expiresIn,
        String refreshToken,
        String scope,
        String tokenType,
        String idToken
) {
}
