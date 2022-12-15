package com.jgm.sns.domain.member.repository;

import com.jgm.sns.domain.member.entity.Member;
import com.jgm.sns.domain.member.entity.MemberNicknameHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberNicknameHistoryRepository {
    final private NamedParameterJdbcTemplate jdbc;
    final static private String TABLE = "membernicknamehistory";

    static final RowMapper<MemberNicknameHistory> rowMapper = (rs, rn) ->
            MemberNicknameHistory.builder()
                    .id(rs.getLong("id"))
                    .memberId(rs.getLong("memberId"))
                    .nickname(rs.getString("nickname"))
                    .createAt(rs.getObject("createAt", LocalDateTime.class))
                    .build();


    public List<MemberNicknameHistory> findAllByMemberId(Long memberId){
        String sql = String.format("SELECT * FROM %s WHERE memberId = :memberId", TABLE);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId);
        return jdbc.query(sql, params, rowMapper);
    }

    public MemberNicknameHistory save(MemberNicknameHistory history) {
        if (history.getId() == null) {
            return insert(history);
        }
        throw new UnsupportedOperationException("MemberNicknameRepository는 갱신을 지원하지 않습니다.");
    }


    public MemberNicknameHistory insert(MemberNicknameHistory history) {
        /**
         * SimpleJdbcInsert가 마음대로 쿼리문을 만들면 setColumnNames로 Column 이름을 지정하자.
         */
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbc.getJdbcTemplate());
        jdbcInsert.setColumnNames(List.of("memberId", "nickname", "createAt"));
        jdbcInsert.withTableName(TABLE).usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(history);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return MemberNicknameHistory.builder()
                .id(id)
                .nickname(history.getNickname())
                .memberId(history.getMemberId())
                .createAt(LocalDateTime.now())
                .build();
    }

}
