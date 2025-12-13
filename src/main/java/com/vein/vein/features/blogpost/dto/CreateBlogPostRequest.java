package com.vein.vein.features.blogpost.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateBlogPostRequest {
    private String content;
}
