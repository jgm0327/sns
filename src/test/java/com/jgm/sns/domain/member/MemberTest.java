package com.jgm.sns.domain.member;

import com.jgm.sns.domain.member.entity.Member;
import com.jgm.sns.domain.member.util.MemberFixtureFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

public class MemberTest {
    @Test
    @DisplayName("회원은 닉네임을 변경할 수 있다.")
    public void testChangeName() {
        Member member = MemberFixtureFactory.create();
        String expected = "pnu";
        member.changeNickname(expected);
        Assertions.assertEquals(expected, member.getNickname());
    }

    @Test
    @DisplayName("회원의 닉네임은 10자를 초과할 수 없다.")
    public void testNicknameMaxLength() {
        Member member = MemberFixtureFactory.create();
        String overMaxLength = "pnupnupnupnu";
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> member.changeNickname(overMaxLength)
        );
    }
}

