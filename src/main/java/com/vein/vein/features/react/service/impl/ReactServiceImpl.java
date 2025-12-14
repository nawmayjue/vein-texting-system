package com.vein.vein.features.react.service.impl;

import com.vein.vein.features.blogpost.repository.jdbc.BlogPostJdbcRepository;
import com.vein.vein.features.blogpost.repository.jpa.BlogPostJpaRepository;
import com.vein.vein.features.react.dto.CreateReactRequest;
import com.vein.vein.features.react.dto.ReactResponse;
import com.vein.vein.features.react.dto.UpdateReactRequest;
import com.vein.vein.features.react.repository.jdbc.ReactJdbcRepository;
import com.vein.vein.features.react.repository.jpa.ReactJpaRepository;
import com.vein.vein.features.react.service.ReactService;
import com.vein.vein.features.user.dto.UserResponse;
import com.vein.vein.features.user.repository.jdbc.UserJdbcRepository;
import com.vein.vein.features.user.repository.jpa.UserJpaRepository;
import com.vein.vein.shared.data.model.BlogPost;
import com.vein.vein.shared.data.model.React;
import com.vein.vein.shared.data.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReactServiceImpl implements ReactService {
    private final UserJpaRepository userJpaRepository;
    private final ReactJpaRepository reactJpaRepository;
    private final UserJdbcRepository userJdbcRepository;
    private final BlogPostJpaRepository blogPostJpaRepository;
    private final BlogPostJdbcRepository blogPostJdbcRepository;
    private final ReactJdbcRepository reactJdbcRepository;


    @Override
    public ReactResponse createReact(CreateReactRequest createReactRequest, String username) {

        User user = userJpaRepository.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("Cannot find user with username " + username )
        );

        BlogPost blogPost = blogPostJpaRepository.findById(createReactRequest.getBlogPostId()).orElseThrow(
                ()-> new RuntimeException("Cannot find blogpost with id " + createReactRequest.getBlogPostId() )
        );

        if(reactJpaRepository.existsByBlogPost_IdAndReactUser_Id(blogPost.getId(), user.getId())){
            throw new RuntimeException("You can't react to a post more than once");
        }

        if(blogPost.getUser().getId().equals(user.getId())){
            throw new RuntimeException("You can't react to your own post");
        }

        React react = React.builder()
                .reactUser(user)
                .blogPost(blogPost)
                .build();

        React savedReact = reactJpaRepository.save(react);

        UserResponse userResponse = userJdbcRepository.findById(savedReact.getReactUser().getId());
        if(userResponse==null){
            throw new RuntimeException("cannot find user");
        }

        return ReactResponse.builder()
                .id(savedReact.getId())
                .user(userResponse)
                .blogPostId(savedReact.getBlogPost().getId())
                .build();
    }

    @Override
    public List<ReactResponse> retrieveAllReact() {
        return reactJdbcRepository.findAll();
    }

    @Override
    public ReactResponse retrieveReactById(Long id) {
        return reactJdbcRepository.findById(id);
    }

    @Override
    public void deleteReactById(Long id) {
        if(!reactJpaRepository.existsById(id)){
            throw new RuntimeException("React with id " + id + " doesn't exist");
        }
        reactJpaRepository.deleteById(id);
    }

    @Override
    public void updateBlogId(UpdateReactRequest updateReactRequest, Long id) {
        reactJdbcRepository.updateReactBlogId(updateReactRequest.getBlogPostId(), id);
    }
}
