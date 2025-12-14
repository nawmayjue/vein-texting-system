package com.vein.vein.features.comment.controller;

import com.vein.vein.features.comment.dto.CommentResponse;
import com.vein.vein.features.comment.dto.CreateCommentRequest;
import com.vein.vein.features.comment.dto.UpdateCommentMessageRequest;
import com.vein.vein.features.comment.service.CommentService;
import com.vein.vein.features.react.dto.UpdateReactRequest;
import com.vein.vein.shared.data.dto.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vein/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse> createComment(
            @RequestBody CreateCommentRequest createCommentRequest
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        CommentResponse commentResponse = commentService.createComment(createCommentRequest, username);
        return ResponseEntity.ok(
                new ApiResponse(
                        HttpStatus.CREATED.value(),
                        commentResponse,
                        "Created Comment Successfully!"
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> retrieveAllComment(
    ){
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        commentService.retrieveAllComment(),
                        "React All retrieved successfully"
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveCommentById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        commentService.retrieveCommentById(id),
                        "React All retrieved successfully"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCommentById(
            @PathVariable Long id
    ){
        commentService.deleteCommentById(id);
        try {
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(
                    new ApiResponse(
                            HttpStatus.NOT_FOUND.value(),
                            null,
                            e.getMessage()
                    )
            );
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateReact(
            @PathVariable Long id,
            @RequestBody UpdateCommentMessageRequest updateCommentMessageRequest
    ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        commentService.updateCommentMessage(updateCommentMessageRequest, id, username);
        try {
            return ResponseEntity.ok().body(
                    new ApiResponse(
                            200,
                            commentService.retrieveCommentById(id),
                            "Updated comment successfully!"
                    )
            );
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(
                    new ApiResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            null,
                            e.getMessage()
                    )
            );
        }


    }
}
