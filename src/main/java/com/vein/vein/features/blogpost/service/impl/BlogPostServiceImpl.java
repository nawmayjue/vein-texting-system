package com.vein.vein.features.blogpost.service.impl;

import com.vein.vein.features.blogpost.dto.BlogPostResponse;
import com.vein.vein.features.blogpost.dto.BlogPostUpdateRequest;
import com.vein.vein.features.blogpost.dto.CreateBlogPostRequest;
import com.vein.vein.features.blogpost.repository.jdbc.BlogPostJdbcRepository;
import com.vein.vein.features.blogpost.repository.jdbc.impl.BlogPostJdbcRepositoryImpl;
import com.vein.vein.features.blogpost.repository.jpa.BlogPostJpaRepository;
import com.vein.vein.features.blogpost.service.BlogPostService;
import com.vein.vein.features.user.repository.jpa.UserJpaRepository;
import com.vein.vein.shared.data.model.BlogPost;
import com.vein.vein.shared.data.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

    private final UserJpaRepository userJpaRepository;
    private final BlogPostJpaRepository blogPostJpaRepository;
    private final BlogPostJdbcRepository blogPostJdbcRepository;

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

    @Override
    public BlogPostResponse retrieveBlogPostById(Long id) {
        return blogPostJdbcRepository.findById(id);
    }

    @Override
    public List<BlogPostResponse> retrieveAllBlogPost() {
        return blogPostJdbcRepository.findAll();
    }

    @Override
    public void updateBlogPost(BlogPostUpdateRequest blogPostUpdateRequest, Long id) {
        if(!blogPostJpaRepository.existsById(id)){
            throw new RuntimeException("Blog post with id " + id + " doesn't exist");
        }

        blogPostJdbcRepository.updateContent(blogPostUpdateRequest.getContent(), id);

    }

    @Override
    public void deleteBlogPostById(Long id) {
        if(!blogPostJpaRepository.existsById(id)){
            throw new RuntimeException("Blog post with id " + id + " doesn't exist");
        }

        blogPostJpaRepository.deleteById(id);
    }
}
