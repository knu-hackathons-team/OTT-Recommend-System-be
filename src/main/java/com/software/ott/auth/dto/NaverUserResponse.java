package com.software.ott.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record NaverUserResponse(
        String resultCode,
        String message,
        Response response
) {
    public record Response(
            String name,
            String email
    ) {
    }
}
