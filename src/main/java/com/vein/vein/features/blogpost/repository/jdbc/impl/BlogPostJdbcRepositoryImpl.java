package com.vein.vein.features.blogpost.repository.jdbc.impl;

import com.vein.vein.features.blogpost.controller.BlogPostController;
import com.vein.vein.features.blogpost.dto.BlogPostResponse;
import com.vein.vein.features.blogpost.mapper.BlogPostRowMapper;
import com.vein.vein.features.blogpost.repository.jdbc.BlogPostJdbcRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class BlogPostJdbcRepositoryImpl implements BlogPostJdbcRepository {
    private final static BlogPostRowMapper BLOG_POST_ROW_MAPPER= new BlogPostRowMapper();
    private final JdbcTemplate jdbcTemplate;

    private final static String FIND_ALL_QUERY= """
            SELECT 
            b.id AS blogPostId, 
            b.content AS blogPostContent,
            u.id AS userId,
            u.display_name AS userDisplayName,
            u.username AS username,
            u.email AS userEmail
            FROM blog_posts b
            JOIN user u
            """;

    private final static String FIND_BY_ID_QUERY= """
            SELECT 
            b.id AS blogPostId, 
            b.content AS blogPostContent,
            u.id AS userId,
            u.display_name AS userDisplayName,
            u.username AS username,
            u.email AS userEmail
            FROM blog_posts b
            JOIN user u
            WHERE id=?
            """;

    private final static String UPDATE_CONTENT_QUERY= """
            UPDATE blog_posts SET content=? WHERE id=?
            """;

    @Override
    public BlogPostResponse findById(Long id) {
        return this.jdbcTemplate.queryForObject(
                FIND_BY_ID_QUERY,
                BLOG_POST_ROW_MAPPER,
                id
        );
    }

    @Override
    public List<BlogPostResponse> findAll() {
        return this.jdbcTemplate.query(
                FIND_BY_ID_QUERY,
                BLOG_POST_ROW_MAPPER
        );
    }

    @Override
    public void updateContent(String content, Long id) {
        this.jdbcTemplate.update(
                UPDATE_CONTENT_QUERY,
                content,
                id
        );
    }
}
