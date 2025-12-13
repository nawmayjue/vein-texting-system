package com.vein.vein.features.blogpost.repository.jpa;

import com.vein.vein.shared.data.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogPostJpaRepository extends JpaRepository<BlogPost, Long> {
}
