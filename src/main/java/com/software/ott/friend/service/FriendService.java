package com.software.ott.friend.service;

import com.software.ott.common.exception.BadRequestException;
import com.software.ott.common.exception.NotFoundException;
import com.software.ott.friend.FriendStatus;
import com.software.ott.friend.dto.FriendEmailRequest;
import com.software.ott.friend.dto.FriendRequestResponse;
import com.software.ott.friend.dto.FriendResponse;
import com.software.ott.friend.entity.Friend;
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
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void makeFriendRequest(Long memberId, FriendEmailRequest friendEmailRequest) {
        Member requesterMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));
        Member accepterMember = memberRepository.findByEmail(friendEmailRequest.email())
                .orElseThrow(() -> new NotFoundException("email에 해당하는 멤버가 없습니다."));

        if (friendRepository.existsByRequesterIdOrAccepterId(memberId, memberId)) {
            throw new BadRequestException("이미 친구이거나, 친구요청이 존재합니다.");
        }

        friendRepository.save(Friend.builder()
                .requester(requesterMember)
                .accepter(accepterMember)
                .status(FriendStatus.PENDING)
                .build());
    }

    @Transactional(readOnly = true)
    public List<FriendResponse> getAllFriend(Long memberId) {
        return friendRepository.findAllByAccepterIdAndStatusOrRequesterIdAndStatus(memberId, FriendStatus.ACCEPTED, memberId, FriendStatus.ACCEPTED)
                .stream().map(friend -> {
                    if (friend.getRequester().getId().equals(memberId)) {
                        return new FriendResponse(
                                friend.getId(),
                                friend.getAccepter().getName(),
                                friend.getAccepter().getEmail()
                        );
                    } else {
                        return new FriendResponse(
                                friend.getId(),
                                friend.getRequester().getName(),
                                friend.getRequester().getEmail()
                        );
                    }
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendRequestResponse> getAllDeclinedFriends(Long memberId) {
        return friendRepository.findAllByAccepterIdAndStatusOrRequesterIdAndStatus(memberId, FriendStatus.DECLINED, memberId, FriendStatus.DECLINED)
                .stream().map(
                        Friend -> new FriendRequestResponse(
                                Friend.getId(),
                                Friend.getRequester().getName(),
                                Friend.getRequester().getEmail(),
                                Friend.getAccepter().getName(),
                                Friend.getAccepter().getEmail()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendRequestResponse> getAllFriendRequests(Long memberId) {
        return friendRepository.findAllByAccepterIdAndStatusOrRequesterIdAndStatus(memberId, FriendStatus.PENDING, memberId, FriendStatus.PENDING)
                .stream().map(
                        Friend -> new FriendRequestResponse(
                                Friend.getId(),
                                Friend.getRequester().getName(),
                                Friend.getRequester().getEmail(),
                                Friend.getAccepter().getName(),
                                Friend.getAccepter().getEmail()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void acceptFriendRequest(Long memberId, Long friendRequestId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        Friend friendRequest = friendRepository.findById(friendRequestId)
                .orElseThrow(() -> new NotFoundException("friendRequestId에 해당하는 친구요청이 없습니다."));

        if (friendRequest.checkMemberIsNotAccepter(member)) {
            throw new BadRequestException("친구 요청을 수락할 권한이 없습니다. 요청받은 사람만 수락할 수 있습니다.");
        }

        friendRequest.acceptFriendRequest();
    }

    @Transactional
    public void declineFriendRequest(Long memberId, Long friendRequestId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        Friend friendRequest = friendRepository.findById(friendRequestId)
                .orElseThrow(() -> new NotFoundException("friendRequestId에 해당하는 친구요청이 없습니다."));

        if (friendRequest.checkMemberIsNotAccepter(member)) {
            throw new BadRequestException("친구 요청을 수락할 권한이 없습니다. 요청받은 사람만 수락할 수 있습니다.");
        }

        friendRequest.declinedFriendRequest();
    }

    @Transactional
    public void deleteFriend(Long memberId, Long friendRequestId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        Friend friendRequest = friendRepository.findById(friendRequestId)
                .orElseThrow(() -> new NotFoundException("friendRequestId에 해당하는 친구요청이 없습니다."));

        if (friendRequest.checkMemberIsNotRequester(member) && friendRequest.checkMemberIsNotAccepter(member)) {
            throw new BadRequestException("친구를 삭제할 권한이 없습니다. 당사자들만 삭제할 수 있습니다.");
        }

        friendRepository.deleteById(friendRequestId);
    }
}
