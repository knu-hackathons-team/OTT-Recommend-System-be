package com.software.ott.friend.service;

import com.software.ott.common.exception.BadRequestException;
import com.software.ott.common.exception.NotFoundException;
import com.software.ott.content.entity.Content;
import com.software.ott.content.repository.ContentRepository;
import com.software.ott.friend.dto.FriendRecommendResponse;
import com.software.ott.friend.dto.RecommendContentRequest;
import com.software.ott.friend.entity.Friend;
import com.software.ott.friend.entity.FriendRecommend;
import com.software.ott.friend.repository.FriendRecommendRepository;
import com.software.ott.friend.repository.FriendRepository;
import com.software.ott.member.entity.Member;
import com.software.ott.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendRecommendService {

    private final FriendRecommendRepository friendRecommendRepository;
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public void sendContentRecommend(Long memberId, Long friendRequestId, RecommendContentRequest recommendContentRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        Friend friend = friendRepository.findById(friendRequestId)
                .orElseThrow(() -> new NotFoundException("friendRequestId에 해당하는 친구 요청(친구)가 없습니다."));

        Content content = contentRepository.findById(recommendContentRequest.contentId())
                .orElseThrow(() -> new NotFoundException("contentId에 해당하는 컨텐츠가 없습니다."));

        if (!friendRepository.existsByRequesterIdOrAccepterId(memberId, friend.getAccepter().getId()) && !friendRepository.existsByRequesterIdOrAccepterId(friend.getRequester().getId(), memberId)) {
            throw new BadRequestException("서로 친구 관계가 아닙니다.");
        }

        Member friendMember = null;

        if (friend.getAccepter().equals(member)) {
            friendMember = friend.getRequester();
        }

        if (friend.getRequester().equals(member)) {
            friendMember = friend.getAccepter();
        }

        if (friendMember == null) {
            throw new BadRequestException("정상적인 접근이 아닙니다.");
        }

        if (friendRecommendRepository.existsBySenderAndReceiverAndRecommendContent(member, friendMember, content)) {
            throw new BadRequestException("이미 해당 컨텐츠를 상대방에게 추천했습니다.");
        }

        friendRecommendRepository.save(
                FriendRecommend.builder()
                        .sender(member)
                        .receiver(friendMember)
                        .recommendContent(content)
                        .reason(recommendContentRequest.reason())
                        .build());
    }

    @Transactional(readOnly = true)
    public List<FriendRecommendResponse> getAllRecommendedContents(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        return friendRecommendRepository.getAllByReceiver(member)
                .stream().map(
                        FriendRecommend -> new FriendRecommendResponse(
                                FriendRecommend.getId(),
                                FriendRecommend.getSender().getName(),
                                FriendRecommend.getSender().getEmail(),
                                FriendRecommend.getRecommendContent(),
                                FriendRecommend.getReason()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRecommend(Long memberId, Long recommendId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        FriendRecommend friendRecommend = friendRecommendRepository.findById(recommendId)
                .orElseThrow(() -> new NotFoundException("recommendId에 해당하는 친구의 추천이 없습니다."));

        if (friendRecommend.NotAuth(member)) {
            throw new BadRequestException("추천 발송인이나 수신인만 삭제가 가능합니다.");
        }

        friendRecommendRepository.delete(friendRecommend);
    }
}
