package com.jgm.sns.controller;

import com.jgm.sns.domain.member.dto.MemberDto;
import com.jgm.sns.domain.member.dto.RegisterMemberCommand;
import com.jgm.sns.domain.member.entity.Member;
import com.jgm.sns.domain.member.service.MemberReadService;
import com.jgm.sns.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {
    final private MemberWriteService memberWriteService;
    final private MemberReadService memberReadService;

    @PostMapping("/members")
    public MemberDto register(@RequestBody RegisterMemberCommand command){
        Member member = memberWriteService.create(command);
        return memberReadService.toDto(member);
    }

    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable Long id){
        return memberReadService.getMember(id);
    }

    @PostMapping("/{id}/name")
    public MemberDto changeNickname(@PathVariable Long id, @RequestBody String nickname){
        memberWriteService.changeNickname(id, nickname);
        return memberReadService.getMember(id);
    }
}
