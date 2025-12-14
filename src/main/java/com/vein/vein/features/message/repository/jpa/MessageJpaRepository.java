package com.vein.vein.features.message.repository.jpa;

import com.vein.vein.shared.data.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageJpaRepository extends JpaRepository<Message, Long> {
}
