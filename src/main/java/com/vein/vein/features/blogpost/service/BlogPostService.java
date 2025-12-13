package com.vein.vein.features.blogpost.service;

import com.vein.vein.features.blogpost.dto.BlogPostResponse;
import com.vein.vein.features.blogpost.dto.BlogPostUpdateRequest;
import com.vein.vein.features.blogpost.dto.CreateBlogPostRequest;

import java.util.List;

public interface BlogPostService {
    BlogPostResponse createBlogPost(CreateBlogPostRequest createBlogPostRequest);
    BlogPostResponse retrieveBlogPostById(Long id);
    List<BlogPostResponse> retrieveAllBlogPost();
    void updateBlogPost(BlogPostUpdateRequest blogPostUpdateRequest, Long id);
    void deleteBlogPostById(Long id);
}
