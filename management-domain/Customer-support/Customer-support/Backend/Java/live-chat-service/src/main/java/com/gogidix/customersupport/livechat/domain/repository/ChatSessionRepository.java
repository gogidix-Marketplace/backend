package com.gogidix.customersupport.livechat.domain.repository;

import com.gogidix.customersupport.livechat.domain.model.ChatSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends MongoRepository<ChatSession, String> {

    List<ChatSession> findByTenantId(String tenantId);

    Optional<ChatSession> findByTenantIdAndId(String tenantId, String id);

    Optional<ChatSession> findBySessionId(String sessionId);

    List<ChatSession> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<ChatSession> findByTenantIdAndAssignedAgentId(String tenantId, String agentId);

    List<ChatSession> findByTenantIdAndStatus(String tenantId, ChatSession.ChatStatus status);

    List<ChatSession> findByTenantIdAndStatusOrderByStartedAtDesc(String tenantId, ChatSession.ChatStatus status);

    List<ChatSession> findByTenantIdAndStartedAtBetween(String tenantId, Instant startDate, Instant endDate);

    List<ChatSession> findByTenantIdAndAssignedAgentIdAndStatus(String tenantId, String agentId, ChatSession.ChatStatus status);

    Optional<ChatSession> findFirstByTenantIdAndAssignedAgentIdAndStatusOrderByStartedAtDesc(
            String tenantId, String agentId, ChatSession.ChatStatus status);

    List<ChatSession> findByTenantIdAndCategory(String tenantId, String category);

    void deleteByTenantIdAndId(String tenantId, String id);

    boolean existsBySessionId(String sessionId);

    long countByTenantIdAndStatus(String tenantId, ChatSession.ChatStatus status);

    long countByTenantIdAndAssignedAgentIdAndStatus(String tenantId, String agentId, ChatSession.ChatStatus status);
}
