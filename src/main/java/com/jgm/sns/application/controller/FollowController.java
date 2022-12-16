package com.jgm.sns.application.controller;

import com.jgm.sns.application.usecase.CreateFollowUsecase;
import com.jgm.sns.application.usecase.GetFollowingMembersUsecase;
import com.jgm.sns.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    final private CreateFollowUsecase followUsecase;
    final private GetFollowingMembersUsecase getFollowingMembersUsecase;

    @PostMapping("/{fromId}/{toId}")
    public void create(@PathVariable Long fromId, @PathVariable Long toId) {
        followUsecase.execute(fromId, toId);
    }

    @GetMapping("/members/{fromId}")
    public List<MemberDto> getFollowings(@PathVariable Long fromId){
        return getFollowingMembersUsecase.execute(fromId);
    }
}
