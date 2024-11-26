package com.software.ott.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleUserResponse(
        String id,
        String email,
        String name,
        String givenName,
        String familyName,
        String picture,
        String locale
) {
}
