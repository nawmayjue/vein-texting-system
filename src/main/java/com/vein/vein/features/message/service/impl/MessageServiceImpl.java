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

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageJpaRepository messageJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public MessageResponse sendMessage(CreateMessageRequest createMessageRequest, String senderUsername) {
        User sender = userJpaRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender user not found"));

        User receiver = userJpaRepository.findById(createMessageRequest.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver user not found"));

        Message message = Message.builder()
                .messageContent(createMessageRequest.getMessageContent())
                .sender(sender)
                .receiver(receiver)
                .build();

        Message savedMessage = messageJpaRepository.save(message);

        return MessageResponse.builder()
                .id(savedMessage.getId())
                .messageContent(savedMessage.getMessageContent())
                .senderId(savedMessage.getSender().getId())
                .senderUsername(savedMessage.getSender().getUsername())
                .receiverId(savedMessage.getReceiver().getId())
                .receiverUsername(savedMessage.getReceiver().getUsername())
                .createdAt(savedMessage.getCreatedAt())
                .build();
    }

    @Override
    public List<MessageResponse> getConversation(String username, Long otherUserId) {
        User currentUser = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Message> messages = messageJpaRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedAtAsc(
                        currentUser.getId(), otherUserId, otherUserId, currentUser.getId()
                );

        return messages.stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> getAllUserMessages(String username) {
        User user = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Message> messages = messageJpaRepository
                .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(user.getId(), user.getId());

        return messages.stream()
                .map(this::mapToMessageResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse mapToMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .messageContent(message.getMessageContent())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .receiverId(message.getReceiver().getId())
                .receiverUsername(message.getReceiver().getUsername())
                .createdAt(message.getCreatedAt())
                .build();
    }
}

