package com.jgm.sns.application.usecase;

import com.jgm.sns.domain.follow.entity.Follow;
import com.jgm.sns.domain.follow.service.FollowReadService;
import com.jgm.sns.domain.follow.service.FollowWriteService;
import com.jgm.sns.domain.post.dto.PostCommand;
import com.jgm.sns.domain.post.service.PostWriteService;
import com.jgm.sns.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreatePostUsecase {

    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final TimelineWriteService timelineWriteService;

    public Long execute(PostCommand postCommand) {
        Long postId = postWriteService.create(postCommand);
        List<Long> followMemberIds = followReadService.getFollowers(postCommand.memberId())
                .stream().map(Follow::getFromMemberId).toList();
        timelineWriteService.deliverytoTimeline(postId, followMemberIds);

        return postId;
    }
}
