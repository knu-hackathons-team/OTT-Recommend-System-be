package com.software.ott.history.repository;

import com.software.ott.history.entity.ContentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentLikeRepository extends JpaRepository<ContentLike, Long> {
    Optional<ContentLike> findByMemberIdAndContentId(Long memberId, Long contentId);

    List<ContentLike> findAllByMemberIdAndLiked(Long memberId, boolean liked);
}
