package com.vein.vein.features.blogpost.repository.jdbc;

import com.vein.vein.features.blogpost.controller.BlogPostController;
import com.vein.vein.features.blogpost.dto.BlogPostResponse;

import java.util.List;

public interface BlogPostJdbcRepository {
    BlogPostResponse findById(Long id);
    List<BlogPostResponse> findAll();
    void updateContent(String content, Long id);
}
