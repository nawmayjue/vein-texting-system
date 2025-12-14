package com.vein.vein.features.react.repository.jpa;

import com.vein.vein.shared.data.model.React;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactJpaRepository extends JpaRepository<React, Long> {
    boolean existsByBlogPost_IdAndReactUser_Id(Long blogPostId, Long reactUserId);
}
