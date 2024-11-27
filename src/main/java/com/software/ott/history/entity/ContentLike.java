package com.software.ott.history.entity;

import com.software.ott.content.entity.Content;
import com.software.ott.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "content_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Content content;
}
