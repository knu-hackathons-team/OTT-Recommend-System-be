package com.software.ott.friend.repository;

import com.software.ott.content.entity.Content;
import com.software.ott.friend.entity.FriendRecommend;
import com.software.ott.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRecommendRepository extends JpaRepository<FriendRecommend, Long> {

    boolean existsBySenderAndReceiverAndRecommendContent(Member sender, Member receiver, Content recommendContent);

    List<FriendRecommend> getAllByReceiver(Member receiver);
}
