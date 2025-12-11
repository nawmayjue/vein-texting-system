package com.vein.vein.features.user.service;

import com.vein.vein.features.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse retrieveUserById(Long id);
    List<UserResponse> retrieveAllUsers();
    void deleteUserById(Long id);
}
