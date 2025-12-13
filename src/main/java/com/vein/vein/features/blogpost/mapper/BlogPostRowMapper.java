package com.vein.vein.features.blogpost.mapper;

import com.vein.vein.features.blogpost.dto.BlogPostResponse;
import com.vein.vein.features.user.dto.UserResponse;
import com.vein.vein.shared.data.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BlogPostRowMapper implements RowMapper<BlogPostResponse> {
    @Override
    public BlogPostResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BlogPostResponse(
                rs.getLong("blogPostId"),
                rs.getString("blogPostContent"),
                new UserResponse(
                        rs.getLong("userId"),
                        rs.getString("userDisplayName"),
                        rs.getString("username"),
                        rs.getString("userEmail")
                )
        );
    }
}
