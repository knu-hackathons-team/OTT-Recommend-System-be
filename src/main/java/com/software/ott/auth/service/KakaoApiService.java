package com.software.ott.auth.service;


import com.software.ott.auth.dto.KakaoTokenResponse;
import com.software.ott.auth.dto.KakaoUserResponse;
import com.software.ott.common.exception.BadRequestException;
import com.software.ott.common.exception.NotFoundException;
import com.software.ott.common.properties.KakaoProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class KakaoApiService {

    private static final String KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com/oauth";
    private static final String KAKAO_API_BASE_URL = "https://kapi.kakao.com/v2/user";
    private static final String LOCALHOST_URL = "localhost:5173";
    private static final String LOCALHOST_URL_IP = "127.0.0.1:5173";
    private static final String SUB_SERVER_URL = "http://ott.backapi.site/redirection";
    private static final String SUB_SERVER_URL_WITHOUT_HTTP = "ott.backapi.site";


    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;

    public String getAuthorizationUrl(HttpServletRequest httpServletRequest) {
        String originHeader = httpServletRequest.getHeader("Origin");
        String refererHeader = httpServletRequest.getHeader("Referer");

        String redirectUri = getRedirectUriBasedOnRequest(originHeader, refererHeader);

        if (redirectUri == null) {
            String hostHeader = httpServletRequest.getHeader("Host");
            redirectUri = getRedirectUriBasedOnRequest(hostHeader, null);
        }

        if (redirectUri == null) {
            throw new BadRequestException("해당 도메인에서는 카카오 로그인이 불가합니다.");
        }

        return KAKAO_AUTH_BASE_URL + "/authorize?response_type=code&client_id="
                + kakaoProperties.clientId() + "&redirect_uri=" + redirectUri;
    }

    private String getRedirectUriBasedOnRequest(String primaryUrl, String secondaryUrl) {
        if (isAllowedDomain(primaryUrl) || isAllowedDomain(secondaryUrl)) {
            return kakaoProperties.redirectUri();
        } else if (isLocalDomain(primaryUrl) || isLocalDomain(secondaryUrl)) {
            return kakaoProperties.devRedirectUri();
        } else if (isSubAllowedDomain(primaryUrl) || isSubAllowedDomain(secondaryUrl)) {
            return SUB_SERVER_URL;
        }
        return null;
    }

    private boolean isAllowedDomain(String url) {
        return url != null && url.contains(kakaoProperties.frontUriWithoutHttp());
    }

    private boolean isLocalDomain(String url) {
        return url != null && (url.contains(LOCALHOST_URL) || url.contains(LOCALHOST_URL_IP));
    }

    private boolean isSubAllowedDomain(String url) {
        return url != null && url.contains(SUB_SERVER_URL_WITHOUT_HTTP);
    }

    public KakaoTokenResponse getAccessToken(String authorizationCode, HttpServletRequest httpServletRequest) {
        String originHeader = httpServletRequest.getHeader("Origin");
        String refererHeader = httpServletRequest.getHeader("Referer");

        String redirectUri = getRedirectUriBasedOnRequest(originHeader, refererHeader);

        if (redirectUri == null) {
            String hostHeader = httpServletRequest.getHeader("Host");
            redirectUri = getRedirectUriBasedOnRequest(hostHeader, null);
        }

        if (redirectUri == null) {
            throw new BadRequestException("해당 도메인에서는 카카오 로그인이 불가합니다.");
        }

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        RequestEntity<LinkedMultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(KAKAO_AUTH_BASE_URL + "/token"));

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(request, KakaoTokenResponse.class);

        return response.getBody();
    }

    public KakaoTokenResponse refreshAccessToken(String refreshToken) {
        String url = KAKAO_AUTH_BASE_URL + "/token";
        String body = "grant_type=refresh_token&client_id=" + kakaoProperties.clientId()
                + "&refresh_token=" + refreshToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, KakaoTokenResponse.class);

        return response.getBody();
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        String url = KAKAO_API_BASE_URL + "/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, request, KakaoUserResponse.class);

        if (response.getBody().kakaoAccount().email() == null) {
            throw new NotFoundException("카카오 계정으로부터 전달받은 이메일이 없습니다.");
        }

        return response.getBody();
    }
}
