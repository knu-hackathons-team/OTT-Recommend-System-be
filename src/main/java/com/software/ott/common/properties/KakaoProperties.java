package com.software.ott.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
        String clientId,
        String redirectUri,
        String devRedirectUri,
        String frontUriWithoutHttp,
        String frontUrl,
        String defaultImageUrl
) {
}
