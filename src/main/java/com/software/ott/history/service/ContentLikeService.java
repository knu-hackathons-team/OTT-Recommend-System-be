package com.software.ott.history.service;

import com.software.ott.common.exception.BadRequestException;
import com.software.ott.common.exception.NotFoundException;
import com.software.ott.content.entity.Content;
import com.software.ott.content.repository.ContentRepository;
import com.software.ott.history.dto.ContentLikeResponse;
import com.software.ott.history.dto.TopContentLikeResponse;
import com.software.ott.history.entity.ContentLike;
import com.software.ott.history.repository.ContentLikeRepository;
import com.software.ott.member.entity.Member;
import com.software.ott.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentLikeService {

    private final ContentLikeRepository contentLikeRepository;
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public void selectLikeDislike(Long memberId, Long contentId, boolean like) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 컨텐츠가 없습니다."));

        ContentLike contentLike = contentLikeRepository.findByMemberIdAndContentId(memberId, contentId)
                .orElse(null);

        if (contentLike == null) {
            ContentLike newContentLike = ContentLike.builder()
                    .content(content)
                    .member(member)
                    .liked(like)
                    .build();
            contentLikeRepository.save(newContentLike);
        } else {
            contentLike.setLiked(like);
        }
    }

    @Transactional
    public List<ContentLikeResponse> readAllLikeContentsByMember(Long memberId, boolean like) {
        return contentLikeRepository.findAllByMemberIdAndLiked(memberId, like)
                .stream()
                .map(ContentLike -> new ContentLikeResponse(
                        ContentLike.getContent()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TopContentLikeResponse> getTop10MostLikedContents() {
        Pageable top10 = PageRequest.of(0, 10);
        List<Object[]> topContents = contentLikeRepository.findTopMostLikedContents(top10);

        return topContents.stream()
                .map(result -> {
                    Content content = (Content) result[0];
                    long likeCount = (long) result[1];

                    return new TopContentLikeResponse(content, likeCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteContentLike(Long memberId, Long contentId) {
        ContentLike contentLike = contentLikeRepository.findByMemberIdAndContentId(memberId, contentId)
                        .orElseThrow(() -> new BadRequestException("선호도를 삭제할 권한이 없습니다."));

        contentLikeRepository.delete(contentLike);
    }
}
