package com.vein.vein.features.message.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageResponse {
    private Long id;
    private String messageContent;
    private Long senderId;
    private String senderUsername;
    private Long receiverId;
    private String receiverUsername;
    private LocalDateTime createdAt;
}

