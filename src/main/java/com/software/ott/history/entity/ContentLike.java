package com.software.ott.history.entity;

import com.software.ott.content.entity.Content;
import com.software.ott.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_contentlike_member_id", columnList = "member_id"),
                @Index(name = "idx_contentlike_content_id", columnList = "content_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Boolean liked;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;
}
