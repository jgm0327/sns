package com.jgm.sns.domain.post.repository;

import com.jgm.sns.domain.post.dto.DailyPostCount;
import com.jgm.sns.domain.post.dto.DailyPostCountRequest;
import com.jgm.sns.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostRepository {

    static final String TABLE = "post";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<DailyPostCount> ROW_MAPPER =
            (rs, rn) -> new DailyPostCount(
                    rs.getLong("memberId"),
                    rs.getObject("createdDate", LocalDate.class),
                    rs.getLong("count"));

    public Post save(Post post){
        if(post.getId() == null){
            return insert(post);
        }
        throw new UnsupportedOperationException("post는 갱신을 하지 않습니다.");
    }

    private Post insert(Post post){
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Post.builder()
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdAt(post.getCreatedAt())
                .createdDate(post.getCreatedDate())
                .build();
    }

    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request){
        String sql = String.format("""
                SELECT createdDate, memberId, count(id) as count FROM %s
                WHERE memberId = :memberId AND createdDate between :firstDate AND :lastDate
                Group by memberId, createdDate
                """, TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(request);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }
}
