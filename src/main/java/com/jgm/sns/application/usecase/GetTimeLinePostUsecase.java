package com.jgm.sns.application.usecase;

import com.jgm.sns.domain.follow.entity.Follow;
import com.jgm.sns.domain.follow.service.FollowReadService;
import com.jgm.sns.domain.post.entity.Post;
import com.jgm.sns.domain.post.entity.Timeline;
import com.jgm.sns.domain.post.service.PostReadService;
import com.jgm.sns.domain.post.service.TimelineReadService;
import com.jgm.sns.util.CursorRequest;
import com.jgm.sns.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTimeLinePostUsecase {

    final private FollowReadService followReadService;
    final private PostReadService postReadService;

    final private TimelineReadService timelineReadService;
    public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest){
        List<Follow> followings = followReadService.getFollowings(memberId);
        List<Long> followingMemberId = followings.stream().map(Follow::getToMemberId).toList();
        return postReadService.getPosts(followingMemberId, cursorRequest);
    }

    public PageCursor<Post> executeByTimeLine(Long memberId, CursorRequest cursorRequest){
        PageCursor<Timeline> timelines = timelineReadService.getTimelines(memberId, cursorRequest);
        List<Long> postIds = timelines.body().stream().map(Timeline::getPostId).toList();
        List<Post> posts = postReadService.getPosts(postIds);
        return new PageCursor<>(timelines.nextCursorRequest(), posts);
    }
}
