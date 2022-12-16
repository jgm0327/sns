package com.jgm.sns.domain.post.service;

import com.jgm.sns.domain.post.dto.PostCommand;
import com.jgm.sns.domain.post.entity.Post;
import com.jgm.sns.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostWriteService {
    final private PostRepository postRepository;

    public Long create(PostCommand command){
        Post post = Post.builder()
                .memberId(command.memberId())
                .contents(command.contents())
                .build();
        return postRepository.save(post).getId();
    }
}
