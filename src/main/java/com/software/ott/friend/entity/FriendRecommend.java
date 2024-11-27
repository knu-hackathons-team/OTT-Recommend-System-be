package com.software.ott.friend.entity;

import com.software.ott.content.entity.Content;
import com.software.ott.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FriendRecommend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;
    @NotNull
    @OneToOne
    @JoinColumn(name = "content_id")
    private Content recommendContent;
    @CreatedDate
    @NotNull
    private LocalDateTime recommendTime;
    private String reason;

    public boolean NotAuth(Member member) {
        return !(sender.equals(member) || receiver.equals(member));
    }
}
