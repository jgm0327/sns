package com.jgm.sns.util;

import com.jgm.sns.domain.post.entity.Post;
import org.springframework.data.domain.Page;

public record CursorRequest(Long key, int size) {
    public static final Long NONE_KEY = -1L;

    public Boolean hasKey(){
        return key != null;
    }

    public CursorRequest next(Long key){
        return new CursorRequest(key, size);
    }
}
