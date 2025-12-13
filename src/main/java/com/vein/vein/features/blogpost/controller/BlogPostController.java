package com.vein.vein.features.blogpost.controller;

import com.vein.vein.features.blogpost.dto.BlogPostResponse;
import com.vein.vein.features.blogpost.dto.BlogPostUpdateRequest;
import com.vein.vein.features.blogpost.dto.CreateBlogPostRequest;
import com.vein.vein.features.blogpost.service.BlogPostService;
import com.vein.vein.shared.data.dto.ApiResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vein/blogposts")
@AllArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping
    public ResponseEntity<ApiResponse> createBlogPost(
            @RequestBody CreateBlogPostRequest createBlogPostRequest
    ){
        BlogPostResponse blogPostResponse = blogPostService.createBlogPost(createBlogPostRequest);
        return ResponseEntity.ok (
                new ApiResponse(
                    201,
                    blogPostResponse,
                    "Blog post created successfully"
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveBlogPostById(
            @PathVariable Long id
    ){
        try {
            return ResponseEntity.ok(
                    new ApiResponse(
                            201,
                            blogPostService.retrieveBlogPostById(id),
                            "Here is the BlogPost with id " + id
                    )
            );
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(
                    new ApiResponse(
                            400,
                            e,
                            ""
                    )
            );
        }
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> retrieveAllBlogPost(
    ){
            return ResponseEntity.ok(
                    new ApiResponse(
                            201,
                            blogPostService.retrieveAllBlogPost(),
                            "Here are all the BlogPosts"
                    )
            );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBlogPostById(
            @PathVariable Long id
    ){
            try {
                blogPostService.deleteBlogPostById(id);
                return ResponseEntity.noContent().build();
            } catch (RuntimeException e){
                return ResponseEntity.badRequest().body(
                        new ApiResponse(
                                400,
                                e,
                                ""
                        )
                );
            }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBlogPostContent(
            @PathVariable Long id,
            @RequestBody BlogPostUpdateRequest blogUpdateRequest
    ){
        try {
            blogPostService.updateBlogPost(blogUpdateRequest, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(
                    new ApiResponse(
                            400,
                            e,
                            ""
                    )
            );
        }
    }
}
