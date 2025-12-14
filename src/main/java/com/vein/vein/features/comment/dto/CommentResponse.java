package com.vein.vein.features.comment.dto;

import com.vein.vein.features.user.dto.UserResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String comment;
    private UserResponse user;
    private Long blogPostId;
}
