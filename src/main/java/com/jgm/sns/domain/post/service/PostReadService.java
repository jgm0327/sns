package com.jgm.sns.domain.post.service;

import com.jgm.sns.domain.post.dto.DailyPostCount;
import com.jgm.sns.domain.post.dto.DailyPostCountRequest;
import com.jgm.sns.domain.post.entity.Post;
import com.jgm.sns.domain.post.repository.PostRepository;
import com.jgm.sns.util.CursorRequest;
import com.jgm.sns.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadService {
    final private PostRepository postRepository;

    public List<DailyPostCount> getDailyPostCount(DailyPostCountRequest request) {
        return postRepository.groupByCreatedDate(request);
    }

    public Page<Post> getPosts(Long memberId, Pageable pageRequest) {
        return postRepository.findAllByMemberId(memberId, pageRequest);
    }

    public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest) {
        List<Post> posts = findAllBy(memberId, cursorRequest);
        Long nextKey = getNextKey(posts);
        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    public PageCursor<Post> getPosts(List<Long> memberIds, CursorRequest cursorRequest) {
        List<Post> posts = findAllBy(memberIds, cursorRequest);
        Long nextKey = getNextKey(posts);
        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    public List<Post> getPosts(List<Long> ids){
        return postRepository.findAllByInId(ids);
    }

    private static Long getNextKey(List<Post> posts) {
        Long nextKey = posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
        return nextKey;
    }

    private List<Post> findAllBy(Long memberId, CursorRequest cursorRequest) {
        if (cursorRequest.hasKey()) {
            return postRepository.findByAllByLessThanMemberIdAndOrderByIdDesc(memberId,
                    cursorRequest.key(), cursorRequest.size());
        }
        return postRepository.findByAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());

    }

    private List<Post> findAllBy(List<Long> memberIds, CursorRequest cursorRequest) {
        if (memberIds.isEmpty()) return List.of();

        if (cursorRequest.hasKey()) {
            return postRepository.findByAllByLessThanMemberIdAndOrderByIdDesc(memberIds,
                    cursorRequest.key(), cursorRequest.size());
        }
        return postRepository.findByAllByMemberIdAndOrderByIdDesc(memberIds, cursorRequest.size());

    }
}
