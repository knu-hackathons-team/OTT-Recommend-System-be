package com.software.ott.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "common")
public record CommonProperties(
        String phoneNumber
) {
}
