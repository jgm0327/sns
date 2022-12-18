package com.jgm.sns.domain.post;

import com.jgm.sns.domain.member.util.PostFixtureFactory;
import com.jgm.sns.domain.post.entity.Post;
import com.jgm.sns.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {
    @Autowired
    private PostRepository postRepository;

    @DisplayName("insert bulk data to post table")
    @Test
    public void bulkInsert() {
        var easyRandom = PostFixtureFactory.get(4L,
                LocalDate.of(1970, 1, 1),
                LocalDate.of(2022, 2, 1));

        var stopWatch = new StopWatch();

        var posts = IntStream.range(0, 10000 * 100)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .toList();
        postRepository.bulkInsert(posts);
    }
}
