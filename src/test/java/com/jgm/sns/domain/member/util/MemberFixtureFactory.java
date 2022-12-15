package com.jgm.sns.domain.member.util;

import com.jgm.sns.domain.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class MemberFixtureFactory {

    static public Member create(){
        EasyRandomParameters parameters = new EasyRandomParameters();
        return new EasyRandom(parameters).nextObject(Member.class);
    }
    static public Member create(Long seed){
        EasyRandomParameters parameters = new EasyRandomParameters().seed(seed);
        return new EasyRandom(parameters).nextObject(Member.class);
    }
}
