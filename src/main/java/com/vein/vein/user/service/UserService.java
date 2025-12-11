package com.vein.vein.user.service;

import com.vein.vein.shared.data.model.User;
import com.vein.vein.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse retrieveUserById(Long id);
    List<UserResponse> retrieveAllUsers();
    void deleteUserById(Long id);
}
