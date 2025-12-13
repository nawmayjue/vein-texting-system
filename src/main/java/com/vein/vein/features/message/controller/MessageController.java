package com.vein.vein.features.message.controller;

import com.vein.vein.features.message.dto.CreateMessageRequest;
import com.vein.vein.features.message.dto.MessageResponse;
import com.vein.vein.features.message.service.MessageService;
import com.vein.vein.shared.data.dto.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vein/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse> sendMessage(
            @RequestBody CreateMessageRequest createMessageRequest
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        MessageResponse messageResponse = messageService.sendMessage(createMessageRequest, username);
        return ResponseEntity.ok(
                new ApiResponse(
                        201,
                        messageResponse,
                        "Message sent successfully"
                )
        );
    }

    @GetMapping("/conversation/{otherUserId}")
    public ResponseEntity<ApiResponse> getConversation(
            @PathVariable Long otherUserId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<MessageResponse> messages = messageService.getConversation(username, otherUserId);
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        messages,
                        "Conversation retrieved successfully"
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllMessages() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<MessageResponse> messages = messageService.getAllUserMessages(username);
        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        messages,
                        "Messages retrieved successfully"
                )
        );
    }
}

