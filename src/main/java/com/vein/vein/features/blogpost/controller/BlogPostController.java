package com.vein.vein.features.blogpost.controller;

import com.vein.vein.features.blogpost.dto.BlogPostResponse;
import com.vein.vein.features.blogpost.dto.CreateBlogPostRequest;
import com.vein.vein.features.blogpost.service.BlogPostService;
import com.vein.vein.shared.data.dto.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vein/blogposts")
@AllArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<ApiResponse> createBlogPost(
            @RequestBody CreateBlogPostRequest createBlogPostRequest
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        BlogPostResponse blogPostResponse = blogPostService.createBlogPost(createBlogPostRequest, username);
        return ResponseEntity.ok (
                new ApiResponse(
                    201,
                    blogPostResponse,
                    "Blog post created successfully"
            )
        );
    }
}
