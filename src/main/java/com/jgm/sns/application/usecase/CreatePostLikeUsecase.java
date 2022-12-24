package com.jgm.sns.application.usecase;

import com.jgm.sns.domain.member.dto.MemberDto;
import com.jgm.sns.domain.member.service.MemberReadService;
import com.jgm.sns.domain.post.entity.Post;
import com.jgm.sns.domain.post.service.PostLikeWriteService;
import com.jgm.sns.domain.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreatePostLikeUsecase {
    private final PostReadService postReadService;
    private final MemberReadService memberReadService;
    private final PostLikeWriteService postLikeWriteService;

    public void execute(Long postId, Long memberId) {
        Post post = postReadService.getPost(postId);
        MemberDto member = memberReadService.getMember(memberId);
        postLikeWriteService.create(post, member);
    }
}
