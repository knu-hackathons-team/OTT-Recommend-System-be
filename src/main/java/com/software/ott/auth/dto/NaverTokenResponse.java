package com.software.ott.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverTokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        String expiresIn
) {
}
