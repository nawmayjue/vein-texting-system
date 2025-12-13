package com.vein.vein.features.message.handler;

import com.vein.vein.features.message.dto.ChatMessage;
import com.vein.vein.features.message.dto.CreateMessageRequest;
import com.vein.vein.features.message.service.MessageService;
import com.vein.vein.features.user.repository.jpa.UserJpaRepository;
import com.vein.vein.shared.data.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@AllArgsConstructor
@Slf4j
public class ChatWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final UserJpaRepository userJpaRepository;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // Get authenticated user from security context or header accessor
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            // Fallback: try to get from header accessor if SecurityContext is null
            if (authentication == null && headerAccessor.getUser() != null) {
                authentication = (Authentication) headerAccessor.getUser();
            }
            
            String senderUsername = authentication != null ? authentication.getName() : chatMessage.getSender();
            
            if (senderUsername == null || senderUsername.equals("anonymousUser")) {
                log.error("Cannot determine sender username. Authentication: {}, ChatMessage sender: {}", 
                         authentication, chatMessage.getSender());
                throw new RuntimeException("User not authenticated. Please provide a valid JWT token.");
            }
            
            // Create message request
            CreateMessageRequest createMessageRequest = CreateMessageRequest.builder()
                    .messageContent(chatMessage.getContent())
                    .receiverId(chatMessage.getReceiverId())
                    .build();

            // Save message to database
            var messageResponse = messageService.sendMessage(createMessageRequest, senderUsername);

            // Update chat message with saved data
            chatMessage.setSender(senderUsername);
            chatMessage.setSenderId(messageResponse.getSenderId());
            chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            chatMessage.setType("MESSAGE");

            // Get receiver user to get their username for routing
            User receiver = userJpaRepository.findById(chatMessage.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver user not found"));
            
            // Get sender user to get their username for routing
            User sender = userJpaRepository.findById(messageResponse.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender user not found"));

            // Send to specific user (receiver) - use username for routing
            String receiverDestination = "/user/" + receiver.getUsername() + "/queue/messages";
            log.info("Sending message to receiver destination: {}", receiverDestination);
            messagingTemplate.convertAndSendToUser(
                    receiver.getUsername(),
                    "/queue/messages",
                    chatMessage
            );
            log.info("Message sent to receiver: {} (username: {})", receiver.getUsername(), receiver.getUsername());

            // Also send back to sender for confirmation - use username for routing
            String senderDestination = "/user/" + sender.getUsername() + "/queue/messages";
            log.info("Sending message to sender destination: {}", senderDestination);
            messagingTemplate.convertAndSendToUser(
                    sender.getUsername(),
                    "/queue/messages",
                    chatMessage
            );
            log.info("Message sent to sender: {} (username: {})", sender.getUsername(), sender.getUsername());
            
            log.info("Message sent from {} (ID: {}) to {} (ID: {})", 
                    senderUsername, messageResponse.getSenderId(), 
                    receiver.getUsername(), chatMessage.getReceiverId());

        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage(), e);
        }
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in websocket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setType("JOIN");
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return chatMessage;
    }

    @MessageMapping("/chat.typing")
    public void typing(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Fallback: try to get from header accessor if SecurityContext is null
        if (authentication == null && headerAccessor.getUser() != null) {
            authentication = (Authentication) headerAccessor.getUser();
        }
        
        String senderUsername = authentication != null ? authentication.getName() : chatMessage.getSender();
        
        chatMessage.setSender(senderUsername);
        chatMessage.setType("TYPING");
        
        // Get receiver user to get their username for routing
        User receiver = userJpaRepository.findById(chatMessage.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver user not found"));
        
        messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/typing",
                chatMessage
        );
    }
}

