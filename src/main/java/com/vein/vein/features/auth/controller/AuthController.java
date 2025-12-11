package com.vein.vein.features.auth.controller;

import com.vein.vein.features.auth.dto.LoginResponse;
import com.vein.vein.features.user.dto.AuthRequest;
import com.vein.vein.features.user.dto.UserRegisterRequest;
import com.vein.vein.features.user.service.JwtService;
import com.vein.vein.features.user.service.impl.UserServiceImpl;
import com.vein.vein.shared.data.dto.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vein/auth")
@AllArgsConstructor
public class AuthController {
    private final UserServiceImpl service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> addNewUser(
            @RequestBody UserRegisterRequest userInfo) {
        String response = service.addUser(userInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(201, true, response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(
                    new ApiResponse(
                            200,
                            new LoginResponse(token),
                            "Login successful!"
                    )
            );
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
