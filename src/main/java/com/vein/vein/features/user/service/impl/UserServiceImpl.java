package com.vein.vein.features.user.service.impl;

import com.vein.vein.shared.data.model.User;
import com.vein.vein.features.user.dto.UserRegisterRequest;
import com.vein.vein.features.user.dto.UserResponse;
import com.vein.vein.features.user.repository.jdbc.UserJdbcRepository;
import com.vein.vein.features.user.repository.jpa.UserJpaRepository;
import com.vein.vein.features.user.service.UserInfoDetails;
import com.vein.vein.features.user.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder encoder;
    private final UserJdbcRepository userJdbcRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userJpaRepository.findByUsername(username);
        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
    public String addUser(UserRegisterRequest userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        User user = User.builder()
                .displayName(userInfo.getDisplayName())
                .username(userInfo.getUsername())
                .password(userInfo.getPassword())
                .email(userInfo.getEmail())
                .build();

        userJpaRepository.save(user);
        return "User Added Successfully";
    }

    @Override
    public UserResponse retrieveUserById(Long id) {
        return userJdbcRepository.findById(id);
    }

    @Override
    public List<UserResponse> retrieveAllUsers() {
        return userJdbcRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        if (userJpaRepository.existsById(id)){
            throw new RuntimeException("User with id " + id + " doesn't exist");
        }

        userJpaRepository.deleteById(id);
    }
}
