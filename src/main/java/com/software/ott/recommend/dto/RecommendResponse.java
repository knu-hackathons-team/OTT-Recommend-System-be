package com.software.ott.recommend.dto;


import java.util.List;

public record RecommendResponse(
        List<ContentRecommendation> similarCastFromLikes,
        List<ContentRecommendation> similarGenreFromLikes,
        List<ContentRecommendation> sameDirectorFromLikes,
        List<ContentRecommendation> similarCastFromWatchHistory,
        List<ContentRecommendation> similarGenreFromWatchHistory,
        List<ContentRecommendation> sameDirectorFromWatchHistory
) {
}
