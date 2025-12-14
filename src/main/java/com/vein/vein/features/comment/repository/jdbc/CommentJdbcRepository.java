package com.vein.vein.features.comment.repository.jdbc;

import com.vein.vein.features.comment.dto.CommentResponse;

import java.util.List;

public interface CommentJdbcRepository {
    List<CommentResponse> findAll();
    CommentResponse findById(Long id);
    void updateComment(String comment, Long id);
}
