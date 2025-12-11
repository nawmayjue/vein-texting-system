package com.vein.vein.features.blogpost.service;

import com.vein.vein.features.blogpost.dto.BlogPostResponse;
import com.vein.vein.features.blogpost.dto.CreateBlogPostRequest;

public interface BlogPostService {
    BlogPostResponse createBlogPost(CreateBlogPostRequest createBlogPostRequest);
}
