package com.vein.vein.features.comment.repository.jpa;

import com.vein.vein.shared.data.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
}
