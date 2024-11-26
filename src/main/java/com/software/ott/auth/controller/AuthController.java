package com.software.ott.auth.controller;


import com.software.ott.auth.dto.TokenRefreshRequest;
import com.software.ott.auth.dto.TokenResponse;
import com.software.ott.auth.service.*;
import com.software.ott.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "회원 인증", description = "회원 인증 관련 API")
public class AuthController {

    private final TokenService tokenService;
    private final KakaoApiService kakaoApiService;
    private final MemberService memberService;
    private final PhoneNumberAuthService phoneNumberAuthService;
    private final NaverApiService naverApiService;
    private final GoogleApiService googleApiService;

    public AuthController(TokenService tokenService, KakaoApiService kakaoApiService, MemberService memberService, PhoneNumberAuthService phoneNumberAuthService, NaverApiService naverApiService, GoogleApiService googleApiService) {
        this.tokenService = tokenService;
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
        this.phoneNumberAuthService = phoneNumberAuthService;
        this.naverApiService = naverApiService;
        this.googleApiService = googleApiService;
    }

    @Operation(summary = "토큰 재발급", description = "RefreshToken으로 AccessToken과 RefreshToken을 재발급 한다.", security = @SecurityRequirement(name = "JWT제외"))
    @PostMapping("/auth/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        TokenResponse tokenResponse = tokenService.refreshAccessToken(request.refreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);
    }

    @Operation(summary = "Oauth 카카오 인증페이지 리다이렉트", description = "카카오 로그인 화면으로 이동한다.", security = @SecurityRequirement(name = "JWT제외"))
    @GetMapping("/auth/oauth/kakao")
    public ResponseEntity<Void> redirectToKakaoAuth(HttpServletRequest httpServletRequest) {
        String url = kakaoApiService.getAuthorizationUrl(httpServletRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @Operation(summary = "Oauth 카카오 로그인 콜백", description = "카카오 로그인 이후 발생하는 인가코드를 통해 AccessToken과 RefreshToken을 발급한다.", security = @SecurityRequirement(name = "JWT제외"))
    @GetMapping("/auth/oauth/kakao/callback")
    public ResponseEntity<TokenResponse> kakaoCallback(@RequestParam("code") String code, HttpServletRequest httpServletRequest) {
        TokenResponse loginResponse = memberService.kakaoLogin(code, httpServletRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @Operation(summary = "Oauth 네이버 인증페이지 리다이렉트", description = "네이버 로그인 화면으로 이동한다.", security = @SecurityRequirement(name = "JWT제외"))
    @GetMapping("/auth/oauth/naver")
    public ResponseEntity<Void> redirectToNaverAuth(HttpServletRequest httpServletRequest) {
        String url = naverApiService.getNaverLoginUrl(httpServletRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @Operation(summary = "Oauth 네이버 로그인 콜백", description = "네이버 로그인 이후 발생하는 인가코드를 통해 AccessToken을 발급받고 사용자 정보를 조회한다.", security = @SecurityRequirement(name = "JWT제외"))
    @GetMapping("/auth/oauth/naver/callback")
    public ResponseEntity<TokenResponse> naverCallback(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletRequest httpServletRequest) {
        TokenResponse loginResponse = memberService.naverLogin(code, state, httpServletRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @Operation(summary = "Oauth 구글 인증페이지 리다이렉트", description = "구글 로그인 화면으로 이동한다.", security = @SecurityRequirement(name = "JWT제외"))
    @GetMapping("/auth/oauth/google")
    public ResponseEntity<Void> redirectToGoogleAuth(HttpServletRequest httpServletRequest) {
        String url = googleApiService.getGoogleLoginUrl(httpServletRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @Operation(summary = "Oauth 구글 로그인 콜백", description = "구글 로그인 이후 발생하는 인가코드를 통해 AccessToken을 발급받고 사용자 정보를 조회한다.", security = @SecurityRequirement(name = "JWT제외"))
    @GetMapping("/auth/oauth/google/callback")
    public ResponseEntity<TokenResponse> googleCallback(@RequestParam("code") String code, HttpServletRequest httpServletRequest) {
        TokenResponse loginResponse = memberService.googleLogin(code, httpServletRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @Operation(summary = "전화번호 인증용 qr 생성", description = "사용자 전화번호 인증용 qr을 생성합니다.")
    @GetMapping("/qr")
    public ResponseEntity<byte[]> generateQRCode(@RequestAttribute("memberId") Long memberId, @RequestParam String phoneNumber) {
        byte[] qrCodeImage = phoneNumberAuthService.generateQRCodeAsByteArray(memberId, phoneNumber);
        if (qrCodeImage != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .body(qrCodeImage);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "twilio 웹훅 서버 post용 api", description = "직접 사용 x", security = @SecurityRequirement(name = "JWT제외"))
    @PostMapping("/auth/twilio/sms")
    public ResponseEntity<Void> receiveSms(@RequestParam Map<String, String> params) {
        phoneNumberAuthService.authPhoneNumber(params);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "인증된 전화번호 member 등록", description = "인증된 전화번호를 멤버에 등록합니다.")
    @PostMapping("/save")
    public ResponseEntity<Void> savePhoneNumber(@RequestAttribute("memberId") Long memberId, String phoneNumber) {
        phoneNumberAuthService.addPhoneNumber(memberId, phoneNumber);
        return ResponseEntity.ok().build();
    }
}
