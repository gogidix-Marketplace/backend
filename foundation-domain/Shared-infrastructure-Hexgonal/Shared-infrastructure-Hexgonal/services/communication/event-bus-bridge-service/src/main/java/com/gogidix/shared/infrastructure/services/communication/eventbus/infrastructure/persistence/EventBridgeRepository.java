package com.gogidix.shared.infrastructure.services.communication.eventbus.infrastructure.persistence;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model.EventBridge;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;
public interface EventBridgeRepository extends MongoRepository<EventBridge, String> {
    // Find by tenant
    List<EventBridge> findByTenantId_Value(String tenantId);
    // Find by tenant and ID
    Optional<EventBridge> findByTenantId_ValueAndId(String tenantId, String id);
    // Find by tenant and name
    Optional<EventBridge> findByTenantId_ValueAndName(String tenantId, String name);
    // Find by tenant and status
    List<EventBridge> findByTenantId_ValueAndStatus(String tenantId, String status);
    // Find by tenant and source type
    List<EventBridge> findByTenantId_ValueAndSourceType(String tenantId, String sourceType);
    // Find by tenant and target type
    List<EventBridge> findByTenantId_ValueAndTargetType(String tenantId, String targetType);
    // Find enabled bridges by tenant
    List<EventBridge> findByTenantId_ValueAndEnabledTrue(String tenantId);
    // Delete by tenant and ID
    void deleteByTenantId_ValueAndId(String tenantId, String id);
    // Count by tenant
    long countByTenantId_Value(String tenantId);
    // Check exists by tenant and name
    boolean existsByTenantId_ValueAndName(String tenantId, String name);
}
