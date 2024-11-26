package com.software.ott.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "naver")
public record NaverProperties(
        String clientId,
        String redirectUri,
        String clientSecret,
        String devRedirectUri,
        String frontUriWithoutHttp
) {

}
