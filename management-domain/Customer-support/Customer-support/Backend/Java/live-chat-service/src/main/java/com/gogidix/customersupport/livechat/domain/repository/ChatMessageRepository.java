package com.gogidix.customersupport.livechat.domain.repository;

import com.gogidix.customersupport.livechat.domain.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findByTenantId(String tenantId);

    List<ChatMessage> findBySessionId(String sessionId);

    List<ChatMessage> findBySessionIdOrderBySentAtAsc(String sessionId);

    List<ChatMessage> findByTenantIdAndSessionId(String tenantId, String sessionId);

    List<ChatMessage> findBySessionIdAndSentAtBetween(String sessionId, Instant startDate, Instant endDate);

    List<ChatMessage> findByTenantIdAndSenderId(String tenantId, String senderId);

    List<ChatMessage> findBySessionIdAndSenderType(String sessionId, ChatMessage.SenderType senderType);

    List<ChatMessage> findBySessionIdAndIsDeletedFalse(String sessionId);

    void deleteBySessionId(String sessionId);

    long countBySessionId(String sessionId);

    long countBySessionIdAndSenderType(String sessionId, ChatMessage.SenderType senderType);

    ChatMessage findFirstBySessionIdOrderBySentAtDesc(String sessionId);
}
