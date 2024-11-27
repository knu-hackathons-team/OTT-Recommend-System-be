package com.software.ott.common.service;

import com.software.ott.auth.service.KakaoTokenService;
import com.software.ott.common.properties.KakaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class KakaoMessageService {

    private static final String KAKAO_SEND_ME_BASE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private static final String DEFAULT_IMAGE_URL = "https://ifh.cc/g/mjb5nn.jpg";

    private final KakaoTokenService kakaoTokenService;
    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;

    public void sendRecommendMessage(String email, String senderName, String posterUrl, String contentName, String contentGenre, String contentType, String reason) {
        String accessToken = kakaoTokenService.getValidAccessTokenInServer(email);

        if (accessToken == null) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(accessToken);

        if (posterUrl.isEmpty()) {
            posterUrl = DEFAULT_IMAGE_URL;
        }

        String templateObject = String.format(
                "{" +
                        "\"object_type\": \"feed\"," +
                        "\"content\": {" +
                        "\"title\": \"%s 님께서 보내신 추천 컨텐츠!\"," +
                        "\"description\": \"저희 서비스를 이용해주셔서 감사드립니다.\"," +
                        "\"image_url\": \"%s\"," +
                        "\"image_width\": 640," +
                        "\"image_height\": 640," +
                        "\"link\": {" +
                        "\"web_url\": \"%s\"," +
                        "\"mobile_web_url\": \"%s\"" +
                        "}" +
                        "}," +
                        "\"item_content\": {" +
                        "\"items\": [" +
                        "{\"item\": \"추천자\", \"item_op\": \"%s 님\"}," +
                        "{\"item\": \"제목\", \"item_op\": \"%s\"}," +
                        "{\"item\": \"장르\", \"item_op\": \"%s\"}," +
                        "{\"item\": \"타입\", \"item_op\": \"%s\"}," +
                        "{\"item\": \"추천 이유\", \"item_op\": \"%s\"}" +
                        "]" +
                        "}," +
                        "\"buttons\": [" +
                        "{" +
                        "\"title\": \"서비스 이용하기\"," +
                        "\"link\": {" +
                        "\"web_url\": \"%s\"," +
                        "\"mobile_web_url\": \"%s\"" +
                        "}" +
                        "}" +
                        "]" +
                        "}",
                senderName, posterUrl, kakaoProperties.frontUrl(), kakaoProperties.frontUrl(), senderName, contentName
                , contentGenre, contentType, reason, kakaoProperties.frontUrl(), kakaoProperties.frontUrl()
        );


        String encodedTemplateObject = URLEncoder.encode(templateObject, StandardCharsets.UTF_8);

        RequestEntity<String> request = new RequestEntity<>(
                "template_object=" + encodedTemplateObject,
                headers, HttpMethod.POST, URI.create(KAKAO_SEND_ME_BASE_URL));

        restTemplate.exchange(request, String.class);
    }
}
