package com.vein.vein.features.message.repository.jpa;

import com.vein.vein.shared.data.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageJpaRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderByCreatedAtAsc(
            Long senderId1, Long receiverId1, Long senderId2, Long receiverId2
    );
    
    List<Message> findBySenderIdOrReceiverIdOrderByCreatedAtDesc(Long userId1, Long userId2);
}

