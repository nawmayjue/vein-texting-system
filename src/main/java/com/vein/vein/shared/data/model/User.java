package com.vein.vein.shared.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name")
    private String displayName;
    private String username;
    private String password;
    private String email;

    @Column(name="status_id")
    private Integer statusId;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<BlogPost> blogPosts;

}
