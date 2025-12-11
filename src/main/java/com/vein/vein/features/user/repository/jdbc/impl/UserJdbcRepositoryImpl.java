package com.vein.vein.features.user.repository.jdbc.impl;

import com.vein.vein.features.user.dto.UserResponse;
import com.vein.vein.features.user.mapper.UserRowMapper;
import com.vein.vein.features.user.repository.jdbc.UserJdbcRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class UserJdbcRepositoryImpl implements UserJdbcRepository {
    private final static UserRowMapper USER_ROW_MAPPER= new UserRowMapper();
    private final JdbcTemplate jdbctemplate;

    private static final String FIND_ALL_QUERY= """
            SELECT id,username,password,email FROM user
            """;
    private static final String FIND_BY_ID= """
            SELECT id,username,password,email FROM user WHERE id=?
            """;

    @Override
    public List<UserResponse> findAll() {
        return this.jdbctemplate.query(
                FIND_ALL_QUERY,
                USER_ROW_MAPPER
        );
    }

    @Override
    public UserResponse findById(Long id) {
        return this.jdbctemplate.queryForObject(
                FIND_ALL_QUERY,
                USER_ROW_MAPPER,
                id
        );
    }
}
