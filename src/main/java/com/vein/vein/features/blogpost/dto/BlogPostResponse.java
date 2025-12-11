package com.vein.vein.features.blogpost.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BlogPostResponse {
    private Long id;
    private String content;
}
