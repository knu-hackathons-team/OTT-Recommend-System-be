package com.software.ott.history.entity;

import com.software.ott.content.entity.Content;
import com.software.ott.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_watchhistory_member_id", columnList = "member_id"),
                @Index(name = "idx_watchhistory_content_id", columnList = "content_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @NotNull
    private LocalDateTime watchDateTime;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
