package com.vein.vein.shared.data.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="comments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    private User commentUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private BlogPost blogPost;
}
