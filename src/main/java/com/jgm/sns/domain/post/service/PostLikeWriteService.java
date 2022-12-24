package com.jgm.sns.domain.post.service;

import com.jgm.sns.domain.member.dto.MemberDto;
import com.jgm.sns.domain.post.dto.PostCommand;
import com.jgm.sns.domain.post.entity.Post;
import com.jgm.sns.domain.post.entity.PostLike;
import com.jgm.sns.domain.post.repository.PostLikeRepository;
import com.jgm.sns.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeWriteService {
    final private PostLikeRepository postLikeRepository;

    public Long create(Post post, MemberDto memberDto){
        PostLike postLike = PostLike
                .builder()
                .memberId(memberDto.id())
                .postId(post.getId())
                .build();
        return postLikeRepository.save(postLike).getPostId();
    }

}
