package com.vein.vein.shared.data.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="reacts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class React {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reactUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private BlogPost blogPost;
}
