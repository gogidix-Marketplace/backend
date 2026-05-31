package com.gogidix.shared.infrastructure.services.communication.eventbus.domain.port.out;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model.EventBridge;
import java.util.List;
import java.util.Optional;
/**
 * Output port for EventBridge repository
 */
public interface IEventBridgeRepository {
    EventBridge save(EventBridge entity);
    Optional<EventBridge> findById(String id);
    List<EventBridge> findAllByTenantId(String tenantId);
    Optional<EventBridge> findByIdAndTenantId(String id, String tenantId);
    Optional<EventBridge> findByNameAndTenantId(String name, String tenantId);
    List<EventBridge> findByStatusAndTenantId(String status, String tenantId);
    List<EventBridge> findBySourceTypeAndTenantId(String sourceType, String tenantId);
    List<EventBridge> findByTargetTypeAndTenantId(String targetType, String tenantId);
    List<EventBridge> findEnabledByTenantId(String tenantId);
    long countByTenantId(String tenantId);
    void deleteByIdAndTenantId(String id, String tenantId);
    boolean existsByNameAndTenantId(String name, String tenantId);
}
