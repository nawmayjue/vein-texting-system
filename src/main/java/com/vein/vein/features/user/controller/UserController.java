package com.vein.vein.features.user.controller;

import com.vein.vein.features.user.dto.UserResponse;
import com.vein.vein.features.user.service.UserService;
import com.vein.vein.shared.data.dto.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vein/user")
@AllArgsConstructor
public class UserController {

    public final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse> retrieveAllUsers(){
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        userService.retrieveAllUsers(),
                        "Users retrieved successfully"
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveUserById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        userService.retrieveUserById(id),
                        "User retrieved successfully"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMapping(
           @PathVariable Long id
    ){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
