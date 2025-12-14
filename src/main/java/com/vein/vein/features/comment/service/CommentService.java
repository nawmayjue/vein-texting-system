package com.vein.vein.features.comment.service;

import com.vein.vein.features.comment.dto.CommentResponse;
import com.vein.vein.features.comment.dto.CreateCommentRequest;
import com.vein.vein.features.comment.dto.UpdateCommentMessageRequest;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CreateCommentRequest createCommentRequest, String username);
    List<CommentResponse> retrieveAllComment();
    CommentResponse retrieveCommentById(Long id);
    void deleteCommentById(Long id);
    void updateCommentMessage(UpdateCommentMessageRequest updateCommentMessageRequest, Long id, String username);
}
