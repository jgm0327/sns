package com.jgm.sns.domain.post.service;

import com.jgm.sns.domain.post.dto.PostCommand;
import com.jgm.sns.domain.post.entity.Post;
import com.jgm.sns.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void likePost(Long postId){
        Post post = postRepository.findById(postId, true).orElseThrow();
        post.incrementLikeCount();
        postRepository.save(post);
    }

    public void likePostByOptimisticLock(Long postId){
        Post post = postRepository.findById(postId, false).orElseThrow();
        post.incrementLikeCount();
        postRepository.save(post);
    }
}
