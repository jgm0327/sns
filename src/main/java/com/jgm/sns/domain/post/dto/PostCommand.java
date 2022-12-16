package com.jgm.sns.domain.post.dto;

public record PostCommand(
        Long id,
        Long memberId,
        String contents
) {
}
