package com.software.ott.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "google")
public record GoogleProperties(
        String clientId,
        String redirectUri,
        String clientSecret,
        String devRedirectUri,
        String frontUriWithoutHttp
) {
}
