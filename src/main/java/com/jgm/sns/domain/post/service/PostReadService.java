package com.jgm.sns.domain.post.service;

import com.jgm.sns.domain.post.dto.DailyPostCount;
import com.jgm.sns.domain.post.dto.DailyPostCountRequest;
import com.jgm.sns.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadService {
    final private PostRepository postRepository;

    public List<DailyPostCount> getDailyPostCount(DailyPostCountRequest request) {
        return postRepository.groupByCreatedDate(request);
    }
}
