package com.jgm.sns.application.usecase;

import com.jgm.sns.domain.follow.service.FollowWriteService;
import com.jgm.sns.domain.member.dto.MemberDto;
import com.jgm.sns.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateFollowUsecase {

    // Usecase는 도메인 서비스의 흐름만 제어하는 것이지 로직이 있는 것은 아니다.
    final private MemberReadService memberReadService;
    final private FollowWriteService followWriteService;

    public void execute(Long fromMemberId, Long toMemberId) {
        /**
         * 1. 입력받은 memberId로 회원조회
         * 2. FollowWriteService.create()
         */
        MemberDto fromMember = memberReadService.getMember(fromMemberId);
        MemberDto toMember = memberReadService.getMember(toMemberId);
        followWriteService.create(fromMember, toMember);
    }
}
