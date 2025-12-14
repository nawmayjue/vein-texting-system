package com.vein.vein.features.message.service.impl;

import com.vein.vein.features.message.dto.CreateMessageRequest;
import com.vein.vein.features.message.dto.MessageResponse;
import com.vein.vein.features.message.repository.jpa.MessageJpaRepository;
import com.vein.vein.features.message.service.MessageService;
import com.vein.vein.features.user.repository.jpa.UserJpaRepository;
import com.vein.vein.shared.data.model.Message;
import com.vein.vein.shared.data.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageJpaRepository messageRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public MessageResponse sendMessage(CreateMessageRequest createMessageRequest, String senderUsername) {
        User sender = userJpaRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("User with username " + senderUsername + " doesn't exist"));
        User receiver = userJpaRepository.findById(createMessageRequest.getReceiverId())
                .orElseThrow(() -> new RuntimeException("User with id " + createMessageRequest.getReceiverId() + " doesn't exist"));

        Message message = Message.builder()
                .fromUser(sender)
                .toUser(receiver)
                .messageContent(createMessageRequest.getMessageContent())
                .build();

        Message savedMessage = messageRepository.save(message);

        return MessageResponse.builder()
                .id(savedMessage.getId())
                .messageContent(savedMessage.getMessageContent())
                .senderId(savedMessage.getFromUser().getId())
                .senderUsername(savedMessage.getFromUser().getUsername())
                .receiverId(savedMessage.getToUser().getId())
                .receiverUsername(savedMessage.getToUser().getUsername())
                .build();
    }
}
