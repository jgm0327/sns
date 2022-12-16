package com.jgm.sns.application.usecase;

import com.jgm.sns.domain.follow.entity.Follow;
import com.jgm.sns.domain.follow.service.FollowReadService;
import com.jgm.sns.domain.member.dto.MemberDto;
import com.jgm.sns.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetFollowingMembersUsecase {
    final private MemberReadService memberReadService;
    final private FollowReadService followReadService;

    public List<MemberDto> execute(Long memberId){
        List<Follow> followings = followReadService.getFollowings(memberId);
        List<Long> followingMemberIds = followings.stream()
                .map(Follow::getToMemberId).toList();
        return memberReadService.getMembers(followingMemberIds);
    }
}
