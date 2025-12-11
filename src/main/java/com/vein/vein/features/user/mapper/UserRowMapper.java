package com.vein.vein.features.user.mapper;

import com.vein.vein.features.user.dto.UserResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<UserResponse> {
    @Override
    public UserResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserResponse(
                rs.getLong("id"),
                rs.getString("displayName"),
                rs.getString("username"),
                rs.getString("email")
        );
    }
}
