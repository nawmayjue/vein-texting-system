package com.vein.vein.features.comment.service.impl;

import com.vein.vein.features.blogpost.repository.jdbc.BlogPostJdbcRepository;
import com.vein.vein.features.blogpost.repository.jpa.BlogPostJpaRepository;
import com.vein.vein.features.comment.dto.CommentResponse;
import com.vein.vein.features.comment.dto.CreateCommentRequest;
import com.vein.vein.features.comment.dto.UpdateCommentMessageRequest;
import com.vein.vein.features.comment.repository.jdbc.CommentJdbcRepository;
import com.vein.vein.features.comment.repository.jpa.CommentJpaRepository;
import com.vein.vein.features.comment.service.CommentService;
import com.vein.vein.features.user.dto.UserResponse;
import com.vein.vein.features.user.repository.jdbc.UserJdbcRepository;
import com.vein.vein.features.user.repository.jpa.UserJpaRepository;
import com.vein.vein.shared.data.model.BlogPost;
import com.vein.vein.shared.data.model.Comment;
import com.vein.vein.shared.data.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserJdbcRepository userJdbcRepository;
    private final UserJpaRepository userJpaRepository;
    private final BlogPostJpaRepository blogPostJpaRepository;
    private final BlogPostJdbcRepository blogPostJdbcRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final CommentJdbcRepository commentJdbcRepository;

    @Override
    public CommentResponse createComment(CreateCommentRequest createCommentRequest, String username) {

        User user = userJpaRepository.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("Cannot find user with username " + username )
        );

        BlogPost blogPost = blogPostJpaRepository.findById(createCommentRequest.getBlogPostId()).orElseThrow(
                ()-> new RuntimeException("Cannot find blogpost with id " + createCommentRequest.getBlogPostId() )
        );

        Comment comment = Comment.builder()
                .commentUser(user)
                .commentMessage(createCommentRequest.getCommentMessage())
                .blogPost(blogPost)
                .build();

        Comment savedComment = commentJpaRepository.save(comment);

        UserResponse userResponse = userJdbcRepository.findById(savedComment.getCommentUser().getId());
        if(userResponse==null){
            throw new RuntimeException("cannot find user");
        }

        return CommentResponse.builder()
                .id(savedComment.getId())
                .comment(savedComment.getCommentMessage())
                .user(userResponse)
                .blogPostId(savedComment.getBlogPost().getId())
                .build();
    }

    @Override
    public List<CommentResponse> retrieveAllComment() {
        return commentJdbcRepository.findAll();
    }

    @Override
    public CommentResponse retrieveCommentById(Long id) {
        return commentJdbcRepository.findById(id);
    }

    @Override
    public void deleteCommentById(Long id) {
        if(!commentJpaRepository.existsById(id)){
            throw new RuntimeException("Comment with id " + id + " doesn't exist");
        }
        commentJpaRepository.deleteById(id);
    }

    @Override
    public void updateCommentMessage(UpdateCommentMessageRequest updateCommentMessageRequest, Long id, String username) {
        User user = userJpaRepository.findByUsername(username).orElseThrow(
                ()-> new RuntimeException("Cannot find user with username " + username )
        );

        Comment comment = commentJpaRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Cannot find comment with id " + id)
        );

        if(!comment.getCommentUser().getId().equals(user.getId())){
            throw new RuntimeException("You can't edit other people's comment");
        }

        commentJdbcRepository.updateComment(updateCommentMessageRequest.getCommentMessage(), id);
    }
}
