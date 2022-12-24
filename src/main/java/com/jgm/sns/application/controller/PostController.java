package com.jgm.sns.application.controller;

import com.jgm.sns.application.usecase.CreatePostLikeUsecase;
import com.jgm.sns.application.usecase.CreatePostUsecase;
import com.jgm.sns.application.usecase.GetTimeLinePostUsecase;
import com.jgm.sns.domain.post.dto.DailyPostCount;
import com.jgm.sns.domain.post.dto.DailyPostCountRequest;
import com.jgm.sns.domain.post.dto.PostCommand;
import com.jgm.sns.domain.post.dto.PostDto;
import com.jgm.sns.domain.post.entity.Post;
import com.jgm.sns.domain.post.service.PostReadService;
import com.jgm.sns.domain.post.service.PostWriteService;
import com.jgm.sns.util.CursorRequest;
import com.jgm.sns.util.PageCursor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostController {

    final private PostWriteService postWriteService;
    final private PostReadService postReadService;
    final private GetTimeLinePostUsecase getTimeLinePostUsecase;

    final private CreatePostUsecase createPostUsecase;
    final private CreatePostLikeUsecase createPostLikeUsecase;

    @PostMapping
    public Long create(PostCommand command){
        return createPostUsecase.execute(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCount(@RequestBody DailyPostCountRequest request){
        log.info("request body: " + request.toString());
        return postReadService.getDailyPostCount(request);
    }

    @GetMapping("/members/{memberId}")
    public Page<PostDto> getPosts(
            @PathVariable Long memberId,
            Pageable pageRequest
            ){
        return postReadService.getPosts(memberId, pageRequest);
    }

    @GetMapping("/members/{memberId}/by-cursor")
    public PageCursor<Post> getPostsByCursor(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ){
        return postReadService.getPosts(memberId, cursorRequest);
    }

    @GetMapping("/members/{memberId}/timeline")
    public PageCursor<Post> getTimeLine(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ){
        return getTimeLinePostUsecase.execute(memberId, cursorRequest);
    }

    @PostMapping("/{postId}/like/v1")
    public void likePost(@PathVariable Long postId){
        //postWriteService.likePost(postId);
        postWriteService.likePostByOptimisticLock(postId);
    }

    @PostMapping("/{postId}/like/v2")
    public void likePostV2(@PathVariable Long postId, @RequestParam Long memberId){
        createPostLikeUsecase.execute(postId, memberId);
    }

}
