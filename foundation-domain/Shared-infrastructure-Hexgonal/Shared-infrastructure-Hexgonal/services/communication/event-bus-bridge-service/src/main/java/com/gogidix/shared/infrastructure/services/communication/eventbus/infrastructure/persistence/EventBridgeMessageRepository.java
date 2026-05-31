package com.gogidix.shared.infrastructure.services.communication.eventbus.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model.EventBridgeMessage;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;
public interface EventBridgeMessageRepository extends MongoRepository<EventBridgeMessage, String> {
    // Find by tenant
    List<EventBridgeMessage> findByTenantId_Value(String tenantId);
    // Find by tenant and bridge ID
    List<EventBridgeMessage> findByTenantId_ValueAndBridgeId(String tenantId, String bridgeId);
    // Find by tenant and ID
    Optional<EventBridgeMessage> findByTenantId_ValueAndId(String tenantId, String id);
    // Find by tenant and correlation ID
    List<EventBridgeMessage> findByTenantId_ValueAndCorrelationId(String tenantId, String correlationId);
    // Find by tenant and status
    List<EventBridgeMessage> findByTenantId_ValueAndStatus(String tenantId, String status);
    // Find pending messages by tenant
    List<EventBridgeMessage> findByTenantId_ValueAndStatusOrderByCreatedAtAsc(String tenantId, String status);
    // Delete by tenant and ID
    void deleteByTenantId_ValueAndId(String tenantId, String id);
    // Count by tenant
    long countByTenantId_Value(String tenantId);
    // Count by tenant and bridge ID
    long countByTenantId_ValueAndBridgeId(String tenantId, String bridgeId);
    // Count by tenant and status
    long countByTenantId_ValueAndStatus(String tenantId, String status);
    // Delete old messages by tenant and date
    void deleteByTenantId_ValueAndCreatedAtBefore(String tenantId, java.time.LocalDateTime date);
}
