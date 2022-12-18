package com.jgm.sns.domain.member.util;

import com.jgm.sns.domain.post.entity.Post;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.ofType;
import static org.jeasy.random.FieldPredicates.inClass;

public class PostFixtureFactory {

    static public EasyRandom get(Long memberId, LocalDate firstDate, LocalDate lastDate) {

        var idField = FieldPredicates.named("id")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        var memberIdField = FieldPredicates.named("memberId")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        EasyRandomParameters param = new EasyRandomParameters()
                .excludeField(idField)
                .dateRange(firstDate, lastDate)
                .randomize(memberIdField, () -> memberId);
        return new EasyRandom(param);
    }
}
