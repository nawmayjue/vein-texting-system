package com.vein.vein.features.react.dto;

import com.vein.vein.features.blogpost.dto.BlogPostResponse;
import com.vein.vein.features.user.dto.UserResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReactResponse {
    private Long id;
    private UserResponse user;
    private Long blogPostId;
}
