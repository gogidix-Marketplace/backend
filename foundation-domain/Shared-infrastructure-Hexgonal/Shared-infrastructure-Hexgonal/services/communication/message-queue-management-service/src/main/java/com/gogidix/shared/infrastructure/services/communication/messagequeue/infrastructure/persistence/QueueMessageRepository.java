package com.gogidix.shared.infrastructure.services.communication.messagequeue.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.model.QueueMessage;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
public interface QueueMessageRepository extends MongoRepository<QueueMessage, String> {
    // Find by tenant
    List<QueueMessage> findByTenantId_Value(String tenantId);
    // Find by tenant and queue ID
    List<QueueMessage> findByTenantId_ValueAndQueueId(String tenantId, String queueId);
    // Find by tenant and queue name
    List<QueueMessage> findByTenantId_ValueAndQueueName(String tenantId, String queueName);
    // Find by tenant and status
    List<QueueMessage> findByTenantId_ValueAndStatus(String tenantId, String status);
    // Find by tenant and message type
    List<QueueMessage> findByTenantId_ValueAndMessageType(String tenantId, String messageType);
    // Find by tenant and priority
    List<QueueMessage> findByTenantId_ValueAndPriority(String tenantId, String priority);
    // Find by tenant and deduplication ID
    List<QueueMessage> findByTenantId_ValueAndDeduplicationId(String tenantId, String deduplicationId);
    // Delete by tenant and ID
    void deleteByTenantId_ValueAndId(String tenantId, String id);
    // Count by tenant
    long countByTenantId_Value(String tenantId);
    // Count by tenant and queue ID
    long countByTenantId_ValueAndQueueId(String tenantId, String queueId);
    // Count by tenant and status
    long countByTenantId_ValueAndStatus(String tenantId, String status);
    // Find pending messages by tenant and queue (ready to process)
    @Query("{'tenantId.value': ?0, 'queueId': ?1, 'status': 'PENDING', '$or': [{'delayUntil': null}, {'delayUntil': {$lte: ?2}}], '$or': [{'visibilityUntil': null}, {'visibilityUntil': {$lte: ?2}}]}")
    List<QueueMessage> findPendingByTenantAndQueue(String tenantId, String queueId, long now);
    // Find messages that should become visible
    @Query("{'tenantId.value': ?0, 'status': 'PROCESSING', 'visibilityUntil': {$lte: ?1}}")
    List<QueueMessage> findTimeoutMessages(String tenantId, long now);
}
