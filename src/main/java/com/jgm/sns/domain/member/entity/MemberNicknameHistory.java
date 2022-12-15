package com.jgm.sns.domain.member.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class MemberNicknameHistory {
    final private Long id;

    final private String nickname;
    final private Long memberId;

    final private LocalDateTime createAt;

    @Builder
    public MemberNicknameHistory(Long id, String nickname, Long memberId, LocalDateTime createAt) {
        this.id = id;
        this.nickname = Objects.requireNonNull(nickname);
        this.memberId = Objects.requireNonNull(memberId);
        this.createAt = createAt;
    }
}
