package com.jgm.sns.domain.follow.repository;

import com.jgm.sns.domain.follow.entity.Follow;
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
public class FollowRepository {

    private final static String TABLE = "follow";

    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<Follow> ROW_MAPPER = (rs, rn) -> Follow.builder()
            .id(rs.getLong("id"))
            .fromMemberId(rs.getLong("fromMemberId"))
            .toMemberId(rs.getLong("toMemberId"))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<Follow> findAllByFromMemberId(Long fromMemberId){
        String sql = String.format("select * from %s where fromMemberId = :fromMemberId", TABLE);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("fromMemberId", fromMemberId);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Follow> findAllByToMemberId(Long toMemberId){
        String sql = String.format("select * from %s where toMemberId = :toMemberId", TABLE);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("toMemberId", toMemberId);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public Follow save(Follow follow) {
        if (follow.getId() == null) {
            return insert(follow);
        }
        throw new UnsupportedOperationException("Follow는 갱신을 지원하지 않습니다.");
    }

    private Follow insert(Follow follow) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource params = new BeanPropertySqlParameterSource(follow);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Follow.builder()
                .id(id)
                .fromMemberId(follow.getFromMemberId())
                .toMemberId(follow.getToMemberId())
                .createdAt(follow.getCreatedAt())
                .build();
    }
}
