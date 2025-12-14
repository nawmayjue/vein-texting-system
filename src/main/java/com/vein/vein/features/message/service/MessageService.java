package com.vein.vein.features.message.service;

import com.vein.vein.features.message.dto.CreateMessageRequest;
import com.vein.vein.features.message.dto.MessageResponse;

public interface MessageService {
    MessageResponse sendMessage(CreateMessageRequest createMessageRequest, String senderUsername);
}
