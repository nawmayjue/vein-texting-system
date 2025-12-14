package com.vein.vein.features.comment.repository.jdbc.impl;

import com.vein.vein.features.comment.dto.CommentResponse;
import com.vein.vein.features.comment.mapper.CommentRowMapper;
import com.vein.vein.features.comment.repository.jdbc.CommentJdbcRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class CommentJdbcRepositoryImpl implements CommentJdbcRepository {
    private final JdbcTemplate jdbcTemplate;
    private static final CommentRowMapper COMMENT_ROW_MAPPER = new CommentRowMapper();

    private static final String BASE_QUERY = """
        SELECT 
        c.id AS commentId,
        c.comment_message AS commentMessage,
        u.id AS userId,
        u.display_name AS userDisplayName,
        u.username AS username,
        u.email AS userEmail,
        c.blog_post_id AS blogPostId
        FROM comments c
        JOIN user u ON u.id=c.comment_user_id
    """;

    private static final String UPDATE_COMMENT_QUERY = """
        UPDATE comments SET comment_message=? WHERE id=?
    """;

    private static final String FIND_ALL_QUERY = BASE_QUERY;
    private static final String FIND_BY_ID_QUERY = BASE_QUERY + " WHERE c.id=?";

    @Override
    public List<CommentResponse> findAll() {
        return this.jdbcTemplate.query(
                FIND_ALL_QUERY,
                COMMENT_ROW_MAPPER
        );
    }

    @Override
    public CommentResponse findById(Long id) {
        return this.jdbcTemplate.queryForObject(
                FIND_BY_ID_QUERY,
                COMMENT_ROW_MAPPER,
                id
        );
    }

    @Override
    public void updateComment(String comment, Long id) {
        this.jdbcTemplate.update(
                UPDATE_COMMENT_QUERY,
                comment,
                id
        );
    }
}
