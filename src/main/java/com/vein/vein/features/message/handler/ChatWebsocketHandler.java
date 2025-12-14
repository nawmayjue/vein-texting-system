package com.vein.vein.features.message.handler;

import com.vein.vein.features.message.dto.ChatMessage;
import com.vein.vein.features.message.dto.CreateMessageRequest;
import com.vein.vein.features.message.dto.MessageResponse;
import com.vein.vein.features.message.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
@Slf4j
public class ChatWebsocketHandler {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload ChatMessage message,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null && headerAccessor.getUser() != null) {
                authentication = (Authentication) headerAccessor.getUser();
            }

            String senderUsername = authentication != null ? authentication.getName() : message.getSender();

            CreateMessageRequest createMessageRequest = CreateMessageRequest.builder()
                            .messageContent(message.getContent())
                            .receiverId(message.getReceiverId())
                            .build();
            MessageResponse messageResponse = messageService.sendMessage(createMessageRequest, senderUsername);
            message.setSender(senderUsername);
            message.setReceiver(messageResponse.getReceiverUsername());

            log.info("Sending message to user {}", messageResponse.getReceiverUsername());
            simpMessagingTemplate.convertAndSendToUser(
                    messageResponse.getReceiverUsername(),
                    "/queue/messages",
                    message
            );

            log.info("Sending message to user {}", messageResponse.getSenderUsername());
            simpMessagingTemplate.convertAndSendToUser(
                    messageResponse.getSenderUsername(),
                    "/queue/messages",
                    message
            );

            log.info("Message sent from {} to {}: {}", senderUsername, messageResponse.getReceiverUsername(), message.getContent());

        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
        }
    }
}
