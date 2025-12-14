package com.vein.vein.features.comment.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentRequest {
    private String commentMessage;
    private Long blogPostId;
}
