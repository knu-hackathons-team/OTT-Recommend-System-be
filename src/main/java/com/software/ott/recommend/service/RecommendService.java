package com.software.ott.recommend.service;

import com.software.ott.content.entity.Content;
import com.software.ott.content.repository.ContentRepository;
import com.software.ott.history.entity.ContentLike;
import com.software.ott.history.entity.WatchHistory;
import com.software.ott.history.repository.ContentLikeRepository;
import com.software.ott.history.repository.WatchHistoryRepository;
import com.software.ott.recommend.dto.ContentRecommendation;
import com.software.ott.recommend.dto.RecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final ContentRepository contentRepository;
    private final ContentLikeRepository contentLikeRepository;
    private final WatchHistoryRepository watchHistoryRepository;

    @Transactional(readOnly = true)
    public RecommendResponse recommendContentsForMember(Long memberId, int count) {
        List<Content> likedContents = contentLikeRepository.findAllByMemberIdAndLiked(memberId, true)
                .stream()
                .map(ContentLike::getContent)
                .toList();

        List<Content> disLikedContents = contentLikeRepository.findAllByMemberIdAndLiked(memberId, false)
                .stream()
                .map(ContentLike::getContent)
                .toList();

        List<Content> watchedContents = watchHistoryRepository.findAllByMemberId(memberId)
                .stream()
                .map(WatchHistory::getContent)
                .toList();

        Set<String> preferredGenres = extractGenres(likedContents);
        Set<String> preferredActors = extractActors(likedContents);
        Set<String> preferredDirectors = extractDirectors(likedContents);

        List<Content> allContents = contentRepository.findAll();

        List<ContentRecommendation> similarCastFromLikes = filterAndMapContent(allContents, preferredActors, disLikedContents, "좋아요한 컨텐츠들과 출연진 유사", count);
        List<ContentRecommendation> similarGenreFromLikes = filterAndMapContent(allContents, preferredGenres, disLikedContents, "좋아요한 컨텐츠들과 장르 유사", count);
        List<ContentRecommendation> sameDirectorFromLikes = filterAndMapContent(allContents, preferredDirectors, disLikedContents, "좋아요한 컨텐츠들과 감독 동일", count);

        List<ContentRecommendation> similarCastFromWatchHistory = filterAndMapContent(allContents, extractActors(watchedContents), disLikedContents, "시청한 컨텐츠들과 출연진 유사", count);
        List<ContentRecommendation> similarGenreFromWatchHistory = filterAndMapContent(allContents, extractGenres(watchedContents), disLikedContents, "시청한 컨텐츠들과 장르 유사", count);
        List<ContentRecommendation> sameDirectorFromWatchHistory = filterAndMapContent(allContents, extractDirectors(watchedContents), disLikedContents, "시청한 컨텐츠들과 감독 동일", count);

        return new RecommendResponse(
                similarCastFromLikes,
                similarGenreFromLikes,
                sameDirectorFromLikes,
                similarCastFromWatchHistory,
                similarGenreFromWatchHistory,
                sameDirectorFromWatchHistory
        );
    }

    private Set<String> extractGenres(List<Content> contents) {
        return contents.stream()
                .flatMap(content -> Arrays.stream(content.getListedIn().split(",")))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    private Set<String> extractActors(List<Content> contents) {
        return contents.stream()
                .flatMap(content -> Arrays.stream(content.getCast().split(",")))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    private Set<String> extractDirectors(List<Content> contents) {
        return contents.stream()
                .map(Content::getDirector)
                .collect(Collectors.toSet());
    }

    private List<ContentRecommendation> filterAndMapContent(List<Content> allContents, Set<String> preferences, List<Content> excludes, String reason, int count) {
        List<Content> shuffledContents = new ArrayList<>(allContents);
        Collections.shuffle(shuffledContents);

        return shuffledContents.stream()
                .filter(content -> !excludes.contains(content))
                .filter(content -> hasOverlap(content, preferences))
                .limit(count)
                .map(content -> new ContentRecommendation(content, reason))
                .toList();
    }

    private boolean hasOverlap(Content content, Set<String> preferences) {
        List<String> shuffledPreferences = new ArrayList<>(preferences);
        Collections.shuffle(shuffledPreferences);

        return shuffledPreferences.stream().anyMatch(pref ->
                content.getListedIn().contains(pref) ||
                        content.getCast().contains(pref) ||
                        content.getDirector().equals(pref));
    }
}
