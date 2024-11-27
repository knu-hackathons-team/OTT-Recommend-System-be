package com.software.ott.history.repository;

import com.software.ott.history.entity.ContentLike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentLikeRepository extends JpaRepository<ContentLike, Long> {
    Optional<ContentLike> findByMemberIdAndContentId(Long memberId, Long contentId);

    List<ContentLike> findAllByMemberIdAndLiked(Long memberId, boolean liked);

    @Query("SELECT cl.content, COUNT(cl) AS likeCount FROM ContentLike cl " +
            "WHERE cl.liked = true " +
            "GROUP BY cl.content " +
            "ORDER BY likeCount DESC")
    List<Object[]> findTopMostLikedContents(Pageable pageable);
}
