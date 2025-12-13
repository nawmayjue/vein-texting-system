package com.vein.vein.features.react.repository.jdbc.impl;

import com.vein.vein.features.react.dto.ReactResponse;
import com.vein.vein.features.react.mapper.ReactRowMapper;
import com.vein.vein.features.react.repository.jdbc.ReactJdbcRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ReactJdbcRepositoryImpl implements ReactJdbcRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final ReactRowMapper REACT_ROW_MAPPER = new ReactRowMapper();

    private static final String BASE_QUERY = """
        SELECT 
        r.id AS reactId,
        u.id AS userId,
        u.display_name AS userDisplayName,
        u.username AS username,
        u.email AS userEmail,
        r.blog_post_id AS blogPostId
        FROM react r
        JOIN user u ON u.id=r.user_id
    """;

    private static final String FIND_ALL_QUERY = BASE_QUERY;
    private static final String FIND_BY_ID_QUERY = BASE_QUERY + " WHERE id=?";

    @Override
    public List<ReactResponse> findAll() {
        return this.jdbcTemplate.query(
                FIND_ALL_QUERY,
                REACT_ROW_MAPPER
        );
    }

    @Override
    public ReactResponse findById(Long id) {
        return this.jdbcTemplate.queryForObject(
                FIND_BY_ID_QUERY,
                REACT_ROW_MAPPER,
                id
        );
    }
}
