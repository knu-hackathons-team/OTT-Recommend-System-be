package com.software.ott.auth.service;

import com.software.ott.auth.dto.GoogleTokenResponse;
import com.software.ott.auth.dto.GoogleUserResponse;
import com.software.ott.common.exception.BadRequestException;
import com.software.ott.common.properties.GoogleProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class GoogleApiService {

    private static final String GOOGLE_AUTH_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String GOOGLE_API_BASE_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private static final String LOCALHOST_URL = "localhost:5173";
    private static final String LOCALHOST_URL_IP = "127.0.0.1:5173";
    private static final String SUB_SERVER_URL = "http://ott.backapi.site/redirection";
    private static final String SUB_SERVER_URL_WITHOUT_HTTP = "ott.backapi.site";

    private final RestTemplate restTemplate;
    private final GoogleProperties googleProperties;

    public String getGoogleLoginUrl(HttpServletRequest httpServletRequest) {
        String originHeader = httpServletRequest.getHeader("Origin");
        String refererHeader = httpServletRequest.getHeader("Referer");

        String redirectUri = getRedirectUriBasedOnRequest(originHeader, refererHeader);

        if (redirectUri == null) {
            String hostHeader = httpServletRequest.getHeader("Host");
            redirectUri = getRedirectUriBasedOnRequest(hostHeader, null);
        }

        if (redirectUri == null) {
            throw new BadRequestException("해당 도메인에서는 구글 로그인이 불가합니다.");
        }

        return UriComponentsBuilder.fromUriString(GOOGLE_AUTH_BASE_URL)
                .queryParam("client_id", googleProperties.clientId())
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile")
                .encode()
                .build()
                .toUriString();
    }

    private String getRedirectUriBasedOnRequest(String primaryUrl, String secondaryUrl) {
        if (isAllowedDomain(primaryUrl) || isAllowedDomain(secondaryUrl)) {
            return googleProperties.redirectUri();
        } else if (isLocalDomain(primaryUrl) || isLocalDomain(secondaryUrl)) {
            return googleProperties.devRedirectUri();
        } else if (isSubAllowedDomain(primaryUrl) || isSubAllowedDomain(secondaryUrl)) {
            return SUB_SERVER_URL;
        }
        return null;
    }

    private boolean isAllowedDomain(String url) {
        return url != null && url.contains(googleProperties.frontUriWithoutHttp());
    }

    private boolean isLocalDomain(String url) {
        return url != null && (url.contains(LOCALHOST_URL) || url.contains(LOCALHOST_URL_IP));
    }

    private boolean isSubAllowedDomain(String url) {
        return url != null && url.contains(SUB_SERVER_URL_WITHOUT_HTTP);
    }

    public GoogleTokenResponse getAccessToken(String authorizationCode, HttpServletRequest httpServletRequest) {
        String originHeader = httpServletRequest.getHeader("Origin");
        String refererHeader = httpServletRequest.getHeader("Referer");

        String redirectUri = getRedirectUriBasedOnRequest(originHeader, refererHeader);

        if (redirectUri == null) {
            String hostHeader = httpServletRequest.getHeader("Host");
            redirectUri = getRedirectUriBasedOnRequest(hostHeader, null);
        }

        if (redirectUri == null) {
            throw new BadRequestException("해당 도메인에서는 구글 로그인이 불가합니다.");
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", googleProperties.clientId());
        body.add("client_secret", googleProperties.clientSecret());
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, UriComponentsBuilder.fromUriString("https://oauth2.googleapis.com/token").build().toUri());

        ResponseEntity<GoogleTokenResponse> response = restTemplate.exchange(request, GoogleTokenResponse.class);

        return response.getBody();
    }

    public GoogleUserResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserResponse> response = restTemplate.exchange(
                GOOGLE_API_BASE_URL, HttpMethod.GET, entity, GoogleUserResponse.class);

        if (response.getBody() == null || response.getBody().email() == null) {
            throw new BadRequestException("구글 계정으로부터 이메일을 받아올 수 없습니다.");
        }

        return response.getBody();
    }
}
