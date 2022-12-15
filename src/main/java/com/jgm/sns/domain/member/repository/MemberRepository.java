package com.jgm.sns.domain.member.repository;

import com.jgm.sns.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    public Member save(Member member);

    public Member insert(Member member);

    public Member update(Member member);

    public Optional<Member> findById(Long id);
}
