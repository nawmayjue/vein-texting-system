package com.vein.vein.user.repository.jdbc;

import com.vein.vein.user.dto.UserResponse;

import java.util.List;

public interface UserJdbcRepository {
    public List<UserResponse> findAll();
    public UserResponse findById(Long id);
}
