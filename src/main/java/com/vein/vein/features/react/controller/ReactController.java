package com.vein.vein.features.react.controller;

import com.vein.vein.features.react.dto.CreateReactRequest;
import com.vein.vein.features.react.dto.ReactResponse;
import com.vein.vein.features.react.service.ReactService;
import com.vein.vein.features.user.dto.UserResponse;
import com.vein.vein.shared.data.dto.ApiResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequestMapping
@RestController
@AllArgsConstructor
public class ReactController {
    private final ReactService reactService;

    @PostMapping
    public ResponseEntity<ApiResponse> createReact(
            @RequestBody CreateReactRequest createReactRequest
            ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ReactResponse reactResponse = reactService.createReact(createReactRequest, username);
        return ResponseEntity.ok(
                new ApiResponse(
                        HttpStatus.CREATED.value(),
                        reactResponse,
                        "Created React"
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> retrieveAllReact(
    ){
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        reactService.retrieveAllReact(),
                        "React All retrieved successfully"
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveReactById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        reactService.retrieveReactById(id),
                        "React All retrieved successfully"
                )
        );
    }
}
