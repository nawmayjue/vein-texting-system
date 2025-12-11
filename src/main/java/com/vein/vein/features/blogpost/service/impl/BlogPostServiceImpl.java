package com.vein.vein.features.blogpost.service.impl;

import com.vein.vein.features.blogpost.dto.BlogPostResponse;
import com.vein.vein.features.blogpost.dto.CreateBlogPostRequest;
import com.vein.vein.features.blogpost.repository.jpa.BlogPostJpaRepository;
import com.vein.vein.features.blogpost.service.BlogPostService;
import com.vein.vein.features.user.repository.jpa.UserJpaRepository;
import com.vein.vein.shared.data.model.BlogPost;
import com.vein.vein.shared.data.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

    private final UserJpaRepository userJpaRepository;
    private final BlogPostJpaRepository blogPostJpaRepository;

    @Override
    public BlogPostResponse createBlogPost(CreateBlogPostRequest createBlogPostRequest) {

        User user = userJpaRepository.findById(createBlogPostRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User with id " + createBlogPostRequest.getUserId() + " doesn't exist"));

        BlogPost blogPost = BlogPost.builder()
                .content(createBlogPostRequest.getContent())
                .user(
                        user
                )
                .build();

        BlogPost savedBlogPost = blogPostJpaRepository.save(blogPost);

        return BlogPostResponse.builder()
                .id(savedBlogPost.getId())
                .content(savedBlogPost.getContent())
                .build();
    }
}
