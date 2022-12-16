package com.jgm.sns.application.controller;

import com.jgm.sns.domain.post.dto.DailyPostCount;
import com.jgm.sns.domain.post.dto.DailyPostCountRequest;
import com.jgm.sns.domain.post.dto.PostCommand;
import com.jgm.sns.domain.post.service.PostReadService;
import com.jgm.sns.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostController {

    final private PostWriteService postWriteService;
    final private PostReadService postReadService;

    @PostMapping
    public Long create(PostCommand command){
        return postWriteService.create(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCount(@RequestBody DailyPostCountRequest request){
        log.info("request body: " + request.toString());
        return postReadService.getDailyPostCount(request);
    }
}
