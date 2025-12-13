package com.vein.vein.features.message.service;

import com.vein.vein.features.message.dto.CreateMessageRequest;
import com.vein.vein.features.message.dto.MessageResponse;

import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(CreateMessageRequest createMessageRequest, String senderUsername);
    List<MessageResponse> getConversation(String username, Long otherUserId);
    List<MessageResponse> getAllUserMessages(String username);
}

