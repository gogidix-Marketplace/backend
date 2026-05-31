package com.gogidix.shared.infrastructure.services.communication.messagequeue.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.model.MessageQueue;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;
public interface MessageQueueRepository extends MongoRepository<MessageQueue, String> {
    // Find by tenant
    List<MessageQueue> findByTenantId_Value(String tenantId);
    // Find by tenant and ID
    Optional<MessageQueue> findByTenantId_ValueAndId(String tenantId, String id);
    // Find by tenant and name
    Optional<MessageQueue> findByTenantId_ValueAndName(String tenantId, String name);
    // Find by tenant and status
    List<MessageQueue> findByTenantId_ValueAndStatus(String tenantId, String status);
    // Find by tenant and type
    List<MessageQueue> findByTenantId_ValueAndType(String tenantId, String type);
    // Find by tenant and region
    List<MessageQueue> findByTenantId_ValueAndRegion(String tenantId, String region);
    // Delete by tenant and ID
    void deleteByTenantId_ValueAndId(String tenantId, String id);
    // Count by tenant
    long countByTenantId_Value(String tenantId);
    // Check exists by tenant and name
    boolean existsByTenantId_ValueAndName(String tenantId, String name);
    // Find active queues by tenant
    @Query("{'tenantId.value': ?0, 'status': 'ACTIVE'}")
    List<MessageQueue> findActiveByTenant(String tenantId);
}
