package com.jgm.sns.domain.post.repository;

import com.jgm.sns.util.PageHelper;
import com.jgm.sns.domain.post.dto.DailyPostCount;
import com.jgm.sns.domain.post.dto.DailyPostCountRequest;
import com.jgm.sns.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

@RequiredArgsConstructor
@Repository
public class PostRepository {

    static final String TABLE = "post";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<Post> ROW_MAPPER = (rs, rn) -> Post.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("memberId"))
            .contents(rs.getString("contents"))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .createdDate(rs.getObject("createdDate", LocalDate.class))
            .build();

    private static final RowMapper<DailyPostCount> DAILY_POST_COUNT_ROW_MAPPER =
            (rs, rn) -> new DailyPostCount(
                    rs.getLong("memberId"),
                    rs.getObject("createdDate", LocalDate.class),
                    rs.getLong("count"));

    public Post save(Post post) {
        if (post.getId() == null) {
            return insert(post);
        }
        throw new UnsupportedOperationException("post는 갱신을 하지 않습니다.");
    }

    private Post insert(Post post) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdAt(post.getCreatedAt())
                .createdDate(post.getCreatedDate())
                .build();
    }

    private Long getCount(Long memberId){
        var sql = String.format("""
                select count(*)
                from %s
                where memberId = :memberId
                """, TABLE);
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {
        var sql = String.format("""
                SELECT * 
                FROM %s 
                WHERE memberId = :memberId
                ORDER BY %s
                LIMIT :size
                OFFSET :offset
                """, TABLE, PageHelper.orderby(pageable.getSort()));
        var params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());
        var posts = namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
        return new PageImpl(posts, pageable, getCount(memberId));
    }

    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request) {
        String sql = String.format("""
                SELECT createdDate, memberId, count(id) as count FROM %s use index (POST_index_member_id_created_date)
                WHERE memberId = :memberId AND createdDate between :firstDate AND :lastDate
                Group by memberId, createdDate
                """, TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(request);

        return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_ROW_MAPPER);
    }

    public void bulkInsert(List<Post> posts) {
        var sql = String.format("""
                INSERT INTO %s (memberId, contents, createdDate, createdAt)
                VALUES (:memberId, :contents, :createdDate, :createdAt)
                """, TABLE);
        SqlParameterSource[] params = posts
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    public List<Post> findAllByInId(List<Long> ids){
        if(ids.isEmpty())return List.of();
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE id IN (:id)
                """, TABLE);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", ids);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findByAllByMemberIdAndOrderByIdDesc(Long memberId, int size){
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                Order By id desc
                LIMIT :size 
                """, TABLE);

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findByAllByMemberIdAndOrderByIdDesc(List<Long> memberIds, int size){
        if(memberIds.isEmpty()){
            return List.of();
        }
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId in (:memberId)
                Order By id desc
                LIMIT :size 
                """, TABLE);

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberIds)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findByAllByLessThanMemberIdAndOrderByIdDesc(Long memberId, Long id, int size){
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId and id < :id
                Order By id desc
                LIMIT :size
                """, TABLE);

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findByAllByLessThanMemberIdAndOrderByIdDesc(List<Long> memberIds, Long id, int size){
        if(memberIds.isEmpty()){
            return List.of();
        }
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId in (:memberId) and id < :id
                Order By id desc
                LIMIT :size
                """, TABLE);

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberIds)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }
}
