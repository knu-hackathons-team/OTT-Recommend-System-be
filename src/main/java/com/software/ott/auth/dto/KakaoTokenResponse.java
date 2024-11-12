package com.software.ott.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenResponse(
        String accessToken,
        String refreshToken,
        @NotNull
        @Positive
        int expiresIn,
        @NotNull
        @Positive
        int refreshTokenExpiresIn

) {

}
