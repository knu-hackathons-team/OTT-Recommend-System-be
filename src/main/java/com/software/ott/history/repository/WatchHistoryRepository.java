package com.software.ott.history.repository;

import com.software.ott.history.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    Optional<WatchHistory> findByMemberIdAndContentId(Long memberId, Long contentId);

    List<WatchHistory> findAllByMemberId(Long memberId);

    @Query("SELECT wh.content, COUNT(wh) AS watchCount FROM WatchHistory wh " +
            "GROUP BY wh.content " +
            "ORDER BY watchCount DESC")
    List<Object[]> findTop10MostWatchedContents();
}
