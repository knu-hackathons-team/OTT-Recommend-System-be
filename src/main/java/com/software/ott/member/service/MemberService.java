package com.software.ott.member.service;


import com.software.ott.auth.dto.*;
import com.software.ott.auth.service.*;
import com.software.ott.common.exception.ConflictException;
import com.software.ott.common.exception.NotFoundException;
import com.software.ott.member.dto.LoginRequest;
import com.software.ott.member.dto.MemberInfoResponse;
import com.software.ott.member.entity.Member;
import com.software.ott.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final KakaoApiService kakaoApiService;
    private final KakaoTokenService kakaoTokenService;
    private final NaverApiService naverApiService;
    private final GoogleApiService googleApiService;

    @Transactional
    public TokenResponse kakaoLogin(String authorizationCode, HttpServletRequest httpServletRequest) {
        KakaoTokenResponse kakaoTokenResponse = kakaoApiService.getAccessToken(authorizationCode, httpServletRequest);
        KakaoUserResponse kakaoUserResponse = kakaoApiService.getUserInfo(kakaoTokenResponse.accessToken());

        String email = kakaoUserResponse.kakaoAccount().email();

        kakaoTokenService.saveKakaoToken(email, kakaoTokenResponse);

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty()) {
            registerNewMember(kakaoUserResponse.kakaoAccount().profile().nickname(), email);
        }

        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse naverLogin(String code, String state, HttpServletRequest httpServletRequest) {
        NaverTokenResponse naverTokenResponse = naverApiService.getAccessToken(code, state, httpServletRequest);
        NaverUserResponse naverUserResponse = naverApiService.getUserInfo(naverTokenResponse.accessToken());

        String email = naverUserResponse.response().email();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty()) {
            registerNewMember(naverUserResponse.response().name(), email);
        }

        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse googleLogin(String code, HttpServletRequest httpServletRequest) {
        GoogleTokenResponse googleTokenResponse = googleApiService.getAccessToken(code, httpServletRequest);
        GoogleUserResponse googleUserResponse = googleApiService.getUserInfo(googleTokenResponse.accessToken());

        String email = googleUserResponse.email();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty()) {
            registerNewMember(googleUserResponse.name(), googleUserResponse.email());
        }

        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);

        return new TokenResponse(accessToken, refreshToken);
    }

    public void registerNewMember(String name, String email) {

        if (memberRepository.existsByEmail(email)) {
            throw new ConflictException("이미 존재하는 이메일입니다.");
        }

        Member newMember = new Member(name, email);
        memberRepository.save(newMember);
    }

    public void deleteMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new NotFoundException("id에 해당하는 멤버가 없습니다.");
        }

        memberRepository.deleteById(memberId);
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        return new MemberInfoResponse(member.getName(), member.getEmail());
    }

    @Transactional
    public TokenResponse tempLogin(LoginRequest loginRequest) { //카카오 로그인 로직 연결 전 임시 사용 메서드
        String email = loginRequest.email();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isEmpty()) {
            registerNewMember(loginRequest.name(), email);
        }

        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);

        return new TokenResponse(accessToken, refreshToken);
    }
}
