package com.jgm.sns.domain.post.repository;

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
public class TimelineRepository {

    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String TABLE = "timeline";

    private final static RowMapper<Timeline> ROW_MAPPER = (rs, rn) -> Timeline.builder()
            .id(rs.getLong("id"))
            .postId(rs.getLong("postId"))
            .memberId(rs.getLong("memberId"))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build();

    public Timeline save(Timeline timeline) {
        if (timeline.getId() == null) {
            return insert(timeline);
        }
        throw new UnsupportedOperationException("타임라인은 바꿀 수 없습니다.");
    }

    public List<Timeline> findAllByMemberIdOrderByIdDesc(Long memberId, int size) {
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY id DESC
                LIMIT :size
                """, TABLE);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Timeline> findAllByLessThanIdOrderByIdDesc(Long id, Long memberId, int size) {
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId and id < :id
                ORDER BY id DESC
                LIMIT :size
                """, TABLE);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }


    private Timeline insert(Timeline timeline) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(timeline);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Timeline.builder()
                .id(id)
                .postId(timeline.getPostId())
                .memberId(timeline.getMemberId())
                .createdAt(timeline.getCreatedAt())
                .build();
    }

    public void bulkInsert(List<Timeline> timelines) {
        String sql = String.format("""
                INSERT INTO %s (memberId, postId, createdAt)
                VALUES (:memberId, :postId, :createdAt)
                """, TABLE);
        SqlParameterSource[] params = timelines.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

}
