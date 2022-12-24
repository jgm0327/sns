package com.jgm.sns.domain.post.repository;

import com.jgm.sns.domain.post.entity.Post;
import com.jgm.sns.domain.post.entity.PostLike;
import com.jgm.sns.domain.post.entity.Timeline;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostLikeRepository {

    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String TABLE = "postLike";

    private final static RowMapper<PostLike> ROW_MAPPER = (rs, rn) -> PostLike.builder()
            .id(rs.getLong("id"))
            .postId(rs.getLong("postId"))
            .memberId(rs.getLong("memberId"))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build();

    public PostLike save(PostLike postLike) {
        if (postLike.getId() == null) {
            return insert(postLike);
        }
        throw new UnsupportedOperationException("타임라인은 바꿀 수 없습니다.");
    }

    public Long getCount(Long postId){
        var sql = String.format("""
                select count(*)
                from %s
                where postId = :postId
                """, TABLE);
        var params = new MapSqlParameterSource()
                .addValue("postId", postId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    private PostLike insert(PostLike postLike) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(postLike);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return PostLike.builder()
                .id(id)
                .postId(postLike.getPostId())
                .memberId(postLike.getMemberId())
                .createdAt(postLike.getCreatedAt())
                .build();
    }


}
