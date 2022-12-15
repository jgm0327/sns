package com.jgm.sns.domain.member.service;

import com.jgm.sns.domain.member.dto.RegisterMemberCommand;
import com.jgm.sns.domain.member.entity.Member;
import com.jgm.sns.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberWriteService {
    final MemberRepository memberRepository;

    public Member create(RegisterMemberCommand command){
        /*
            TODO: 회원정보(이메일, 닉네임, 생년월일)를 등록한다. - 닉네임은 10자를 넘을 수 없다.
            parameter: memberRegisterCommand
            val member = Member.of(memberRegisterCommand)
            memberRepository.save()
         */

        Member member = Member.builder()
                .nickname(command.nickname())
                .birthday(command.birthday())
                .email(command.email())
                .createdAt(LocalDateTime.now())
                .build();
        log.info(member.getNickname() + " " + member.getEmail() + " " + member.getBirthday());
        return memberRepository.save(member);
    }

    public void changeNickname(Long memberId, String nickname){
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.changeNickname(nickname);
        memberRepository.save(member);
    }
}
