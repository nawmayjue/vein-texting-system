package com.vein.vein.shared.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="blog_posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "blogPost", fetch = FetchType.LAZY)
    private List<React> reacts;
}
