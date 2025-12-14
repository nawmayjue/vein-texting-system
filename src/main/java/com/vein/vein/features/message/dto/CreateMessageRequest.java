package com.vein.vein.features.message.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateMessageRequest {
    private String messageContent;
    private Long receiverId;
}
