package com.vein.vein.features.comment.mapper;

import com.vein.vein.features.comment.dto.CommentResponse;
import com.vein.vein.features.user.dto.UserResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<CommentResponse> {
    @Override
    public CommentResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CommentResponse(
                rs.getLong("commentId"),
                rs.getString("commentMessage"),
                new UserResponse(
                        rs.getLong("userId"),
                        rs.getString("userDisplayName"),
                        rs.getString("username"),
                        rs.getString("userEmail")
                ),
                rs.getLong("blogPostId")
        );
    }
}
