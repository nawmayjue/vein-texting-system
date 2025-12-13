package com.vein.vein.features.blogpost.dto;

import com.vein.vein.features.user.dto.UserResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlogPostResponse {
    private Long id;
    private String content;
    private UserResponse userResponse;
}
