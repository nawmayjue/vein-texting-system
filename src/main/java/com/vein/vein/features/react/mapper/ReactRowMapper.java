package com.vein.vein.features.react.mapper;

import com.vein.vein.features.react.dto.ReactResponse;
import com.vein.vein.features.user.dto.UserResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReactRowMapper implements RowMapper<ReactResponse> {
    @Override
    public ReactResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ReactResponse(
                rs.getLong("reactId"),
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
