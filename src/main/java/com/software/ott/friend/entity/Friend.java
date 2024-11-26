package com.software.ott.friend.entity;

import com.software.ott.common.exception.BadRequestException;
import com.software.ott.friend.FriendStatus;
import com.software.ott.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Member requester;
    @ManyToOne
    @JoinColumn(name = "accepter_id")
    private Member accepter;
    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    public boolean checkMemberIsNotAccepter(Member member) {
        return !this.accepter.equals(member);
    }

    public boolean checkMemberIsNotRequester(Member member) {
        return !this.requester.equals(member);
    }

    public void acceptFriendRequest() {
        if (!this.status.equals(FriendStatus.PENDING)) {
            throw new BadRequestException("대기중인 친구 요청이 아닙니다.");
        }
        this.status = FriendStatus.ACCEPTED;
    }

    public void declinedFriendRequest() {
        if (!this.status.equals(FriendStatus.PENDING)) {
            throw new BadRequestException("대기중인 친구 요청이 아닙니다.");
        }
        this.status = FriendStatus.DECLINED;
    }
}
