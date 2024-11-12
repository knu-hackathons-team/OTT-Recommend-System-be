package com.software.ott.member.service;


import com.software.ott.auth.service.TokenService;
import com.software.ott.common.exception.NotFoundException;
import com.software.ott.member.entity.Member;
import com.software.ott.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberTokenService {
    private final TokenService tokenService;
    private final MemberRepository memberRepository;


    public Long getMemberIdByToken(String token) {
        String email = tokenService.extractEmailFromAccessToken(token);
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("이메일에 해당하는 멤버를 찾을 수 없습니다.")
        );
        return member.getId();
    }
}
