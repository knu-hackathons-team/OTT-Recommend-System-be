package com.software.ott.history.service;

import com.software.ott.common.exception.NotFoundException;
import com.software.ott.common.exception.UnauthorizedException;
import com.software.ott.content.entity.Content;
import com.software.ott.content.repository.ContentRepository;
import com.software.ott.history.dto.WatchHistoryResponse;
import com.software.ott.history.entity.WatchHistory;
import com.software.ott.history.repository.WatchHistoryRepository;
import com.software.ott.member.entity.Member;
import com.software.ott.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WatchHistoryService {

    private final MemberRepository memberRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public void saveWatchHistory(Long memberId, Long contentId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 컨텐츠가 없습니다."));

        WatchHistory watchHistory = watchHistoryRepository.findByMemberIdAndContentId(memberId, contentId)
                .orElse(null);

        if (watchHistory == null) {
            WatchHistory newWatchHistory = WatchHistory.builder()
                    .member(member)
                    .content(content)
                    .build();
            watchHistoryRepository.save(newWatchHistory);
        } else {
            watchHistory.setWatchDateTime(LocalDateTime.now());
        }
    }

    @Transactional
    public List<WatchHistoryResponse> readAllWatchHistoryContentsByMember(Long memberId) {
        return watchHistoryRepository.findAllByMemberId(memberId)
                .stream()
                .map(WatchHistory -> new WatchHistoryResponse(
                        WatchHistory.getWatchDateTime(),
                        WatchHistory.getContent()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteWatchHistory(Long memberId, Long contentId) {
        WatchHistory watchHistory = watchHistoryRepository.findByMemberIdAndContentId(memberId, contentId)
                .orElseThrow(() -> new UnauthorizedException("시청기록을 삭제할 권한이 없습니다."));

        watchHistoryRepository.delete(watchHistory);
    }
}
