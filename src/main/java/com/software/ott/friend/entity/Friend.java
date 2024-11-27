package com.software.ott.friend.entity;

import com.software.ott.common.exception.BadRequestException;
import com.software.ott.friend.FriendStatus;
import com.software.ott.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @NotNull
    @ManyToOne
    @JoinColumn(name = "requester_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member requester;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "accepter_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member accepter;
    @NotNull
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
