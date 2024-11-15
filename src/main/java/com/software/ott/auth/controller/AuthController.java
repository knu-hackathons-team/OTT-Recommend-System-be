package com.software.ott.auth.controller;


import com.software.ott.auth.dto.TokenRefreshRequest;
import com.software.ott.auth.dto.TokenResponse;
import com.software.ott.auth.service.KakaoApiService;
import com.software.ott.auth.service.TokenService;
import com.software.ott.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "회원 인증", description = "회원 인증 관련 API")
public class AuthController {

    private final TokenService tokenService;
    private final KakaoApiService kakaoApiService;
    private final MemberService memberService;


    @Operation(summary = "토큰 재발급", description = "RefreshToken으로 AccessToken과 RefreshToken을 재발급 한다.", security = @SecurityRequirement(name = "JWT제외"))
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        TokenResponse tokenResponse = tokenService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);
    }

    @Operation(summary = "Oauth 카카오 인증페이지 리다이렉트", description = "카카오 로그인 화면으로 이동한다.", security = @SecurityRequirement(name = "JWT제외"))
    @GetMapping("/oauth/kakao")
    public ResponseEntity<Void> redirectToKakaoAuth() {
        String url = kakaoApiService.getAuthorizationUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @Operation(summary = "Oauth 카카오 로그인 콜백", description = "카카오 로그인 이후 발생하는 인가코드를 통해 AccessToken과 RefreshToken을 발급한다.", security = @SecurityRequirement(name = "JWT제외"))
    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<TokenResponse> kakaoCallback(@RequestParam("code") String code) {
        TokenResponse loginResponse = memberService.kakaoLogin(code);
        return ResponseEntity.ok().body(loginResponse);
    }
}
