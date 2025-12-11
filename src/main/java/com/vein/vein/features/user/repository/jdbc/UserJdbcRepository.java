package com.vein.vein.features.user.repository.jdbc;

import com.vein.vein.features.user.dto.UserResponse;

import java.util.List;

public interface UserJdbcRepository {
    public List<UserResponse> findAll();
    public UserResponse findById(Long id);
}
