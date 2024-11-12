package com.software.ott.history.repository;

import com.software.ott.history.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    Optional<WatchHistory> findByMemberIdAndContentId(Long memberId, Long contentId);

    List<WatchHistory> findAllByMemberId(Long memberId);
}
