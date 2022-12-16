package com.jgm.sns.domain.member.repository;

import com.jgm.sns.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JdbcMemberRepository{
    final private NamedParameterJdbcTemplate jdbc;
    final static private String TABLE = "member";

    final static private RowMapper<Member> rowMapper = (rs, rn) ->
            Member.builder()
                    .id(rs.getLong("id"))
                    .email(rs.getString("email"))
                    .nickname(rs.getString("nickname"))
                    .birthday(rs.getObject("birthday", LocalDate.class))
                    .createdAt(rs.getObject("createdAt", LocalDateTime.class))
                    .build();


    public Member save(Member member) {
        if (member.getId() == null) {
            return insert(member);
        }
        return update(member);
    }


    public Member insert(Member member) {
        /**
         * SimpleJdbcInsert가 마음대로 쿼리문을 만들면 setColumnNames로 Column 이름을 지정하자.
         */
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbc.getJdbcTemplate());
        jdbcInsert.setColumnNames(List.of("email", "nickname", "birthday", "createdAt"));
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Member.builder()
                .id(id)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .birthday(member.getBirthday())
                .build();
    }


    public Member update(Member member) {
        String sql = String.format("update %s set email = :email, nickname = :nickname, " +
                "birthday = :birthday where id = :id", TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        jdbc.update(sql, params);
        return member;
    }


    public Optional<Member> findById(Long id) {
        /**
         * select * from member where id = id;
         */
        String sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);

        Member member = jdbc.queryForObject(sql, param, rowMapper);
        return Optional.ofNullable(member);
    }

    public List<Member> findAllByIdIn(List<Long> ids){
        if(ids.isEmpty())
            return List.of();
        String sql = String.format("select * from %s where id in (:ids)", TABLE);
        SqlParameterSource params = new MapSqlParameterSource().addValue("ids", ids);
        return jdbc.query(sql, params, rowMapper);
    }

}
