package com.vein.vein.features.blogpost.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogPostUpdateRequest {
    private String content;
}
