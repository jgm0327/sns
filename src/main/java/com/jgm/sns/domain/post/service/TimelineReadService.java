package com.jgm.sns.domain.post.service;

import com.jgm.sns.domain.post.entity.Timeline;
import com.jgm.sns.domain.post.repository.TimelineRepository;
import com.jgm.sns.util.CursorRequest;
import com.jgm.sns.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineReadService {
    private final TimelineRepository timelineRepository;

    public PageCursor<Timeline> getTimelines(Long memberId, CursorRequest cursorRequest) {
        List<Timeline> timelines = findByAllBy(memberId, cursorRequest);
        Long nextKey = timelines.stream()
                .mapToLong(Timeline::getPostId)
                .min().orElse(CursorRequest.NONE_KEY);
        return new PageCursor<>(cursorRequest.next(nextKey), timelines);
    }

    private List<Timeline> findByAllBy(Long memberId, CursorRequest cursorRequest) {
        if (cursorRequest.hasKey()) {
            return timelineRepository.findAllByLessThanIdOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        }
        return timelineRepository.findAllByMemberIdOrderByIdDesc(memberId, cursorRequest.size());
    }

    private static Long getNextKey(List<Timeline> timelines) {
        return timelines.stream()
                .mapToLong(Timeline::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }
}
