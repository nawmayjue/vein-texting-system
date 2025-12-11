package com.vein.vein.user.controller;

import com.vein.vein.user.dto.UserResponse;
import com.vein.vein.user.service.UserService;
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
    public ResponseEntity<List<UserResponse>> retrieveAllUsers(){
        return ResponseEntity.ok(userService.retrieveAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> retrieveUserById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(userService.retrieveUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMapping(
           @PathVariable Long id
    ){
        userService.deleteById(id);
        return ResponseEntity.ok("User with id " + id + " has been deleted");
    }

}
