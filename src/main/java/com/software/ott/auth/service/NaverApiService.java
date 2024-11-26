package com.software.ott.auth.service;

import com.software.ott.auth.dto.NaverTokenResponse;
import com.software.ott.auth.dto.NaverUserResponse;
import com.software.ott.common.exception.BadRequestException;
import com.software.ott.common.properties.NaverProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverApiService {

    private static final String LOCALHOST_URL = "localhost:5173";
    private static final String LOCALHOST_URL_IP = "127.0.0.1:5173";
    private static final String SUB_SERVER_URL = "http://ott.backapi.site/redirection";
    private static final String SUB_SERVER_URL_WITHOUT_HTTP = "ott.backapi.site";

    private final RestTemplate restTemplate;
    private final NaverProperties naverProperties;

    public String getNaverLoginUrl(HttpServletRequest httpServletRequest) {
        String originHeader = httpServletRequest.getHeader("Origin");
        String refererHeader = httpServletRequest.getHeader("Referer");

        String redirectUri = getRedirectUriBasedOnRequest(originHeader, refererHeader);

        if (redirectUri == null) {
            String hostHeader = httpServletRequest.getHeader("Host");
            redirectUri = getRedirectUriBasedOnRequest(hostHeader, null);
        }

        if (redirectUri == null) {
            throw new BadRequestException("해당 도메인에서는 네이버 로그인이 불가합니다.");
        }

        return UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", naverProperties.clientId())
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", UUID.randomUUID().toString())
                .build()
                .toUriString();
    }

    private String getRedirectUriBasedOnRequest(String primaryUrl, String secondaryUrl) {
        if (isAllowedDomain(primaryUrl) || isAllowedDomain(secondaryUrl)) {
            return naverProperties.redirectUri();
        } else if (isLocalDomain(primaryUrl) || isLocalDomain(secondaryUrl)) {
            return naverProperties.devRedirectUri();
        } else if (isSubAllowedDomain(primaryUrl) || isSubAllowedDomain(secondaryUrl)) {
            return SUB_SERVER_URL;
        }
        return null;
    }

    private boolean isAllowedDomain(String url) {
        return url != null && url.contains(naverProperties.frontUriWithoutHttp());
    }

    private boolean isLocalDomain(String url) {
        return url != null && (url.contains(LOCALHOST_URL) || url.contains(LOCALHOST_URL_IP));
    }

    private boolean isSubAllowedDomain(String url) {
        return url != null && url.contains(SUB_SERVER_URL_WITHOUT_HTTP);
    }

    public NaverTokenResponse getAccessToken(String code, String state, HttpServletRequest httpServletRequest) {
        String originHeader = httpServletRequest.getHeader("Origin");
        String refererHeader = httpServletRequest.getHeader("Referer");

        String redirectUri = getRedirectUriBasedOnRequest(originHeader, refererHeader);

        if (redirectUri == null) {
            String hostHeader = httpServletRequest.getHeader("Host");
            redirectUri = getRedirectUriBasedOnRequest(hostHeader, null);
        }

        if (redirectUri == null) {
            throw new BadRequestException("해당 도메인에서는 네이버 로그인이 불가합니다.");
        }

        String tokenRequestUrl = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", naverProperties.clientId())
                .queryParam("client_secret", naverProperties.clientSecret())
                .queryParam("code", code)
                .queryParam("state", state)
                .queryParam("redirect_uri", redirectUri)
                .build()
                .toUriString();

        ResponseEntity<NaverTokenResponse> response = restTemplate.getForEntity(tokenRequestUrl, NaverTokenResponse.class);
        return response.getBody();
    }

    public NaverUserResponse getUserInfo(String accessToken) {
        String url = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<NaverUserResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, NaverUserResponse.class);

        if (response.getBody() != null && response.getBody().response().email() == null) {
            throw new IllegalArgumentException("네이버 계정으로부터 이메일을 받아올 수 없습니다.");
        }

        return response.getBody();
    }
}
