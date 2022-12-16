package com.jgm.sns.domain.follow.service;

import com.jgm.sns.domain.follow.entity.Follow;
import com.jgm.sns.domain.follow.repository.FollowRepository;
import com.jgm.sns.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

// domain의 결합은 낮추는 방향으로 설계
@Service
@RequiredArgsConstructor
public class FollowWriteService {

    final private FollowRepository followRepository;

    public void create(MemberDto fromMember, MemberDto toMember) {
        Assert.isTrue(!fromMember.id().equals(toMember), "자기 자신을 팔로우할 수 없습니다.");

        Follow follow = Follow.builder()
                .fromMemberId(fromMember.id())
                .toMemberId(toMember.id())
                .build();
        followRepository.save(follow);
    }
}
