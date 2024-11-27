package com.software.ott.friend.repository;

import com.software.ott.friend.FriendStatus;
import com.software.ott.friend.entity.Friend;
import com.software.ott.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findAllByAccepterIdAndStatusOrRequesterIdAndStatus(Long accepterId, FriendStatus status1, Long requesterId, FriendStatus status2);

    boolean existsByRequesterAndAccepter(Member requester, Member accepter);
}
